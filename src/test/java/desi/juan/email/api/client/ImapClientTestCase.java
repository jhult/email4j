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
package desi.juan.email.api.client;

import desi.juan.email.Email4JTestCase;
import desi.juan.email.api.Email;
import desi.juan.email.api.client.configuration.ClientConfiguration;
import desi.juan.email.internal.EmailProtocol;
import desi.juan.email.internal.StoredEmail;
import org.junit.Before;
import org.junit.Test;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;

import static desi.juan.email.EmailTestUtils.GOKU_EMAIL;
import static desi.juan.email.EmailTestUtils.getSinglePartTestMessage;
import static desi.juan.email.api.EmailConstants.INBOX_FOLDER;
import static javax.mail.Folder.READ_ONLY;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ImapClientTestCase extends Email4JTestCase {

  private ImapClient client;

  @Before
  public void sendInitialEmailBatch() throws MessagingException, IOException {
    for (int i = 0; i < 10; i++) {
      user.deliver((MimeMessage) getSinglePartTestMessage());
    }
  }

  @Before
  public void createClient() {
    client = new ImapClient(GOKU_EMAIL, PASSWORD, HOST, PORT, new ClientConfiguration());
  }

  @Test
  public void receive() {
    List<Email> emails = client.retrieve(client.getFolder(INBOX_FOLDER, READ_ONLY), true);
    assertThat(emails.size(), is(10));
    emails.forEach(e -> {
      assertThat(e, is(instanceOf(StoredEmail.class)));
      assertBodyContent(e);
      assertSubject(e);
      assertThat(e.getHeaders().size(), is(7));
    });
  }

  @Override
  public String getProtocol() {
    return EmailProtocol.IMAP.getName();
  }
}
