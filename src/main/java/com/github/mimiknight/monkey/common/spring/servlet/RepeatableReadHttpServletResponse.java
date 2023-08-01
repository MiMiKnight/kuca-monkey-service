package com.github.mimiknight.monkey.common.spring.servlet;

import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * 可重复读HttpServletResponse
 * <p>
 * 可重复读取请求体
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-07-31 21:13:03
 */
public class RepeatableReadHttpServletResponse extends HttpServletResponseWrapper {

    private byte[] body;

    public RepeatableReadHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (ArrayUtils.isEmpty(body)) {
            // TODO 待完善
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void write(int b) throws IOException {
                baos.write(b);
            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        String characterEncoding = this.getCharacterEncoding();
        Charset charset = Charset.forName(characterEncoding);
        return new PrintWriter(new OutputStreamWriter(this.getOutputStream(), charset));
    }
}
