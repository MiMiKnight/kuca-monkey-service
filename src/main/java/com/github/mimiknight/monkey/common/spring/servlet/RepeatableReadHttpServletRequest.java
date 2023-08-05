package com.github.mimiknight.monkey.common.spring.servlet;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 可重复读HttpServletRequest
 * <p>
 * 可重复读取请求体
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-07-31 21:13:03
 */
public class RepeatableReadHttpServletRequest extends HttpServletRequestWrapper {

    private byte[] body;

    public RepeatableReadHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (ArrayUtils.isEmpty(body)) {
            body = IOUtils.toByteArray(super.getInputStream());
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        String charsetName = this.getCharacterEncoding();
        Charset charset = Charset.forName(charsetName);
        return new BufferedReader(new InputStreamReader(this.getInputStream(), charset));
    }
}
