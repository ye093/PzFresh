package com.yejy.app.filter;

import com.yejy.app.constant.ErrorCode;
import com.yejy.app.data.LoginRecordMapper;
import com.yejy.app.data.MemberMapper;
import com.yejy.app.model.LoginRecord;
import com.yejy.app.model.Member;
import com.yejy.app.service.MemberService;
import com.yejy.app.service.impl.MemberServiceImpl;
import io.jsonwebtoken.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;


public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {
    @Value("${token.key}")
    private String tokenKey;

    @Value("${token.issuer}")
    private String tokenIssuer;

    @Value("${token.subject}")
    private String tokenSubject;

    @Value("${token.expiration}")
    private Long tokenExpiration;

    @Value("${token.header}")
    private String headerKey;

    @Autowired
    LoginRecordMapper loginRecordMapper;

    @Autowired
    MemberServiceImpl userDetailsService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String token = request.getHeader(headerKey);
        if (token == null || token.length() == 0) {
//            respMessage(res, ErrorCode.AUTH_FAILURE, null, "请先登录");
            chain.doFilter(req, res);
            return;
        }
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(tokenKey)
                    .requireIssuer(tokenIssuer)
                    .requireSubject(tokenSubject)
                    .parseClaimsJws(token);
            Map<String, Object> data = claims.getBody();
            Integer memberId = (Integer) data.get("member_id");
            String mobile = (String) data.get("mobile");
            Integer loginType = (Integer) data.get("source");
            Object loginTime = data.get("login_time");
            if (memberId == null || loginType == null) {
                respMessage(res, ErrorCode.AUTH_FAILURE, null, "请先登录");
                return;
            } else {
                LoginRecord record = loginRecordMapper.getLoginRecordByMemberId(memberId, loginType);
                if (record.getEnable().equals("Y")) {
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);
                        // 生成通过认证
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // 将权限写入本次会话
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    respMessage(res, ErrorCode.LOGIN_OUT, null, "用户已注销，请重新登录");
                    return;
                }
            }
            chain.doFilter(req, res);
        } catch (ExpiredJwtException e) {
            respMessage(res, ErrorCode.AUTH_EXPIRED, null, "token过期，请重新登录");
        } catch (SignatureException e) {
            respMessage(res, ErrorCode.AUTH_FAILURE, null, "无效token，请重新登录");
        } catch (Exception e) {
            respMessage(res, ErrorCode.AUTH_FAILURE, null, "无效token，请重新登录");
        }
    }

    // 返回数据
    private void respMessage(ServletResponse res, int code, Map<String, Object> data, String msg) throws IOException {
        JSONObject object = new JSONObject();
        object.put("code", code);
        object.put("msg", msg);
        object.put("data", data);
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().print(object);
    }
}
