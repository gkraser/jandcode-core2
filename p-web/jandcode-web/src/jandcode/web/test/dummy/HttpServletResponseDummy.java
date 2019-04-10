package jandcode.web.test.dummy;


import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@SuppressWarnings({"deprecation"})
public class HttpServletResponseDummy implements HttpServletResponse {

    private PrintWriter writer;
    private StringWriter stringWriter;
    private String redirect;
    private ServletOutputStreamDummy outStream;

    ////

    public void createOutWriter() {
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
    }

    public String getOutText() {
        if (isOutputStream()) {
            return outStream.getString();
        }
        String s = stringWriter.toString();
        return s;
    }

    ////

    public void addCookie(Cookie cookie) {

    }

    public boolean containsHeader(String s) {
        return false;
    }

    public String encodeURL(String s) {
        return null;
    }

    public String encodeRedirectURL(String s) {
        return null;
    }

    public String encodeUrl(String s) {
        return null;
    }

    public String encodeRedirectUrl(String s) {
        return null;
    }

    public void sendError(int i, String s) throws IOException {

    }

    public void sendError(int i) throws IOException {

    }

    public void sendRedirect(String s) throws IOException {
        redirect = s;
    }

    public String getRedirectURL() {
        return redirect;
    }

    public void setDateHeader(String s, long l) {

    }

    public void addDateHeader(String s, long l) {

    }

    public void setHeader(String s, String s1) {

    }

    public void addHeader(String s, String s1) {

    }

    public void setIntHeader(String s, int i) {

    }

    public void addIntHeader(String s, int i) {

    }

    public void setStatus(int i) {

    }

    public void setStatus(int i, String s) {

    }

    public String getCharacterEncoding() {
        return null;
    }

    public String getContentType() {
        return null;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (outStream == null) {
            outStream = new ServletOutputStreamDummy();
        }
        return outStream;
    }

    public boolean isOutputStream() {
        return outStream != null;
    }

    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    public void setCharacterEncoding(String s) {

    }

    public void setContentLength(int i) {

    }

    public void setContentType(String s) {

    }

    public void setBufferSize(int i) {

    }

    public int getBufferSize() {
        return 0;
    }

    public void flushBuffer() throws IOException {

    }

    public void resetBuffer() {

    }

    public boolean isCommitted() {
        return false;
    }

    public void reset() {

    }

    public void setLocale(Locale locale) {

    }

    public Locale getLocale() {
        return null;
    }

    //////

    public int getStatus() {
        return 0;
    }

    public String getHeader(String name) {
        return null;
    }

    public Collection<String> getHeaders(String name) {
        return null;
    }

    public Collection<String> getHeaderNames() {
        return null;
    }

    public void setContentLengthLong(long len) {
    }

}