//package msahmadjaelanibetest.resolver;
//
//import jakarta.servlet.http.HttpServletRequest;
//import msahmadjaelanibetest.entity.Account;
//import msahmadjaelanibetest.repository.AccountRepository;
//import msahmadjaelanibetest.service.JwtService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//import org.springframework.web.server.ResponseStatusException;
//
//@Component
//public class UserArgumentResolver implements HandlerMethodArgumentResolver {
//    @Autowired
//    private JwtService jwtService;
//    private AccountRepository accountRepository;
//
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        return Account.class.equals(parameter.getParameterType());
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
//        String token = servletRequest.getHeader("X-API-TOKEN");
//        if(token == null){
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unauthorized cui");
//        }
//
//        Account account = accountRepository.findByToken(token)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unauthorized"));
//
//        if (jwtService.isTokenValid(token, account)){
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid Token");
//        }
//
//        return account;
//    }
//}
