package club.hl.community.community.controller;

import club.hl.community.community.dto.AccessTokenDTO;
import club.hl.community.community.dto.GithubUser;
import club.hl.community.community.mapper.UserMapper;
import club.hl.community.community.model.User;
import club.hl.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {
    /*
    * 这是负责认证的controller
    * */
    @Autowired//这个注解可以从ioc里面自动拿东西出来
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")//这个注解可以读取配置文件中的key的值然后赋值给变量
    private String clienId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="state") String state, @RequestParam(name = "code")String code ,HttpServletRequest request ){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clienId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);

        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null){
            //
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //登陆成功写cookie和session
            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";//这样可以重定向到主页(重点redirect返回的事根目录)



        }else {
            //登陆失败，重新登陆
            return "redirect:/";

        }
    }
}
