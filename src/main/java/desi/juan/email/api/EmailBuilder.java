/**
 * The MIT License (MIT)
 *
 * Original work Copyright (c) 2016 Juan Desimoni
 * Modified work Copyright (c) 2017 yx91490
 * Modified work Copyright (c) 2017 Jonathan Hult
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package desi.juan.email.api;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import desi.juan.email.internal.OutgoingEmail;

import java.util.ArrayList;
import java.util.List;

import static desi.juan.email.api.EmailConstants.NO_SUBJECT;

/**
 * Implementation of the builder design pattern to create a new {@link Email} instance.
 */
public final class EmailBuilder {

  private String subject = NO_SUBJECT;
  private List<String> from = new ArrayList<>();
  private List<String> to = new ArrayList<>();
  private List<String> bcc = new ArrayList<>();
  private List<String> cc = new ArrayList<>();
  private Multimap<String, String> headers = ArrayListMultimap.create();
  private List<String> replyTo = new ArrayList<>();
  private List<EmailAttachment> attachments = new ArrayList<>();
  private EmailBody body;

  /**
   * Hide constructor.
   */
  private EmailBuilder() {
  }

  /**
   * @return an instance of this {@link EmailBuilder}.
   */
  public static EmailBuilder newEmail() {
    return new EmailBuilder();
  }

  /**
   * sets the subject of the email that is being built.
   *
   * @param subject the email subject to be set.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder withSubject(String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Adds an {@link Email} "From" address
   *
   * @param fromAddress the from address to be added.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder from(String fromAddress) {
    this.from.add(fromAddress);
    return this;
  }

  /**
   * Adds "To" (primary) recipients to the {@link Email} that is being built.
   *
   * @param toAddresses the "to" addresses to be added.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder to(List<String> toAddresses) {
    this.to.addAll(toAddresses);
    return this;
  }

  /**
   * Adds "Bcc" (blind carbon copy) recipients to the {@link Email} that is being built.
   *
   * @param bccAddresses the "bcc" addresses to be added.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder bcc(List<String> bccAddresses) {
    this.bcc.addAll(bccAddresses);
    return this;
  }

  /**
   * adds a "Bcc" (blind carbon copy) recipients to the {@link Email} that is being built.
   *
   * @param bcc the "bcc" address to be added.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder bcc(String bcc) {
    this.bcc.add(bcc);
    return this;
  }

  /**
   * adds a "to" (primary) recipients to the {@link Email} that is being built.
   *
   * @param to the "to" address to be added.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder to(String to) {
    this.to.add(to);
    return this;
  }

  /**
   * Adds "Cc" (carbon copy) recipients to the {@link Email} that is being built.
   *
   * @param ccAddresses the "cc" addresses to be set.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder cc(List<String> ccAddresses) {
    this.cc.addAll(ccAddresses);
    return this;
  }

  /**
   * Adds a single "Cc" (carbon copy) recipient to the {@link Email} that is being built.
   *
   * @param cc the "cc" address to be set.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder cc(String cc) {
    this.cc.add(cc);
    return this;
  }

  /**
   * sets additional headers to the email that is being built.
   *
   * @param headers the headers to be set.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder withHeaders(Multimap<String, String> headers) {
    this.headers.putAll(headers);
    return this;
  }

  /**
   * Sets an additional header of the {@link Email} that is being built.
   *
   * @param key the key name of the header.
   * @param val the value of the header.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder withHeader(String key, String val) {
    this.headers.put(key, val);
    return this;
  }

  /**
   * Adds "ReplyTo" addresses to the {@link Email} that is being built.
   *
   * @param replyToAddresses the "replyTo" addresses to be set.
   * @return this {@link EmailBuilder}
   */
  public EmailBuilder replyTo(List<String> replyToAddresses) {
    this.replyTo = replyToAddresses;
    return this;
  }

  /**
   * Sets the specified {@link EmailBody} to the {@link Email} that is being built.
   */
  public EmailBuilder withBody(EmailBody body) {
    this.body = body;
    return this;
  }

  /**
   * Sets a plain text {@link EmailBody} to the {@link Email} that is being built.
   */
  public EmailBuilder withBody(String body) {
    this.body = new EmailBody(body);
    return this;
  }

  /**
   * Sets a {@link List} of {@link EmailAttachment}s to bound in the {@link Email} that is being built.
   */
  public EmailBuilder withAttachments(List<EmailAttachment> attachments) {
    this.attachments.addAll(attachments);
    return this;
  }

  /**
   * Adds an {@link EmailAttachment} to the {@link Email} that is being built.
   */
  public EmailBuilder withAttachment(EmailAttachment attachment) {
    this.attachments.add(attachment);
    return this;
  }

  /**
   * Builds the new {@link Email} instance.
   */
  public Email build() {

    if (from.isEmpty()) {
      throw new IllegalStateException(format("%s%s", ERROR, " FROM address"));
    }

    if (to.isEmpty()) {
      throw new IllegalStateException(format("%s%s", ERROR, " TO address(es)"));
    }

    if (body == null) {
      throw new IllegalStateException(format("%s%s", ERROR, " body"));
    }

    return new OutgoingEmail(
        subject,
        from,
        to,
        bcc,
        cc,
        replyTo,
        body,
        attachments,
        headers);
  }

  private static final String ERROR = "Cannot build an Email with no ";
}