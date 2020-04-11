package com.lanying.community.demo.controller;

import com.lanying.community.demo.dto.AccessTokenDto;
import com.lanying.community.demo.dto.GitHubUser;
import com.lanying.community.demo.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
    @Autowired
    public GitHubProvider gitHubProvider;

    @Value("${client.id}")
    private String clientId;
    @Value("${client.secret}")
    private String clientSecret;
    @Value("${redirect.url}")
    private String redirectUrl;

    @GetMapping("/callback")
    public  String callBack(@RequestParam(name="code") String code, @RequestParam(name="state") String state,
                            HttpServletRequest request) {

        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setState(state);
        accessTokenDto.setRedirect_uri(redirectUrl);
        String s = gitHubProvider.getAccessToken(accessTokenDto);
        System.out.println("accessToken is " + s);
        GitHubUser user = gitHubProvider.getUser(s);
        if (null != user) {
            request.getSession().setAttribute("user",user);
            System.out.println(user.getName());
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }

}
