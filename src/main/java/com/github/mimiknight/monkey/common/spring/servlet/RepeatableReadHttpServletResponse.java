package com.github.mimiknight.monkey.common.spring.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 可重复读HttpServletResponse
 * <p>
 * 可重复读取请求体
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @since 2023-07-31 21:13:03
 */
public class RepeatableReadHttpServletResponse extends HttpServletResponseWrapper {

    public RepeatableReadHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return this.getResponse().getOutputStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return this.getResponse().getWriter();
    }
}
