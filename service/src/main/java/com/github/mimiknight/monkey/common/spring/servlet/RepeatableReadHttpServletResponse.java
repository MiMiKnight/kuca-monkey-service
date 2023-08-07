package com.github.mimiknight.monkey.common.spring.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.apache.commons.lang3.ArrayUtils;

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

    private final ByteArrayOutputStream baos;

    private ServletOutputStream outputStream;

    public RepeatableReadHttpServletResponse(HttpServletResponse response) {
        super(response);
        baos = new ByteArrayOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        this.outputStream = this.getResponse().getOutputStream();
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return outputStream.isReady();
            }

            @Override
            public void setWriteListener(WriteListener listener) {
                outputStream.setWriteListener(listener);
            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
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

    /**
     * 获取字节数组响应体
     *
     * @return {@link byte[]}
     */
    public byte[] getByteBody() {
        return baos.toByteArray();
    }

    /**
     * 获取响应体
     *
     * @return {@link String}
     */
    public String getBody() {
        byte[] bytes = getByteBody();
        if (ArrayUtils.isEmpty(bytes)) {
            return "";
        }
        String characterEncoding = this.getCharacterEncoding();
        Charset charset = Charset.forName(characterEncoding);
        return new String(bytes, charset);
    }

}
