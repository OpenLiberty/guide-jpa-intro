package io.openliberty.guides.eventapp.ui;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;


import io.openliberty.guides.eventapp.models.User;
import io.openliberty.guides.eventapp.facelets.PageDispatcher;
import io.openliberty.guides.eventapp.lists.UserList;
import io.openliberty.guides.eventapp.ui.util.SessionUtils;
import io.openliberty.guides.eventapp.resources.UserService;
import com.ibm.websphere.security.web.WebSecurityHelper;

import com.ibm.websphere.security.jwt.*;


@ManagedBean
@ViewScoped
public class LoginBean {

  private String username;
  private String password;

  List<String> revokeList = readConfig();

  @ManagedProperty(value = "#{pageDispatcher}")
  public PageDispatcher pageDispatcher;

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public PageDispatcher getPageDispatcher() {
    return pageDispatcher;
  }

  public void setPageDispatcher(PageDispatcher pageDispatcher) {
    this.pageDispatcher = pageDispatcher;
  }

  public String doLogIn() {
    String returnPage = "eventmanager.jsf";

    HttpServletRequest request = SessionUtils.getRequest();

    // do filter
    if (revokeList.contains(username)) {
      System.out.println("User is blocked.");
      pageDispatcher.showLogBlocked();
      return returnPage;
    }

    // do login
    try {
      request.login(this.username, this.password);
    } catch (ServletException e) {
      //context.addMessage(null, new FacesMessage("Login failed."));
      System.out.println("Login failed.");
      pageDispatcher.showLogError();
      return returnPage;
    }

    // to get remote user using getRemoteUser()
    String remoteUser = request.getRemoteUser();
    String role = getRole(request);
    System.out.println("AFTER LOGIN, REMOTE USER: " + remoteUser + " " + role);

    // update session
    if (remoteUser != null && remoteUser.equals(username)){
        buildJWT(request, role);
        updateSessionUser(request, role);
    } else {
      System.out.println("Update Sessional User Failed.");
    }
    //System.out.println("LTPA cookie" + getSecurityTokenLiberty());
    //consumeJWT();

    pageDispatcher.showMainPage();
    return returnPage;
  }

  private void consumeJWT() {

     try {
			JwtConsumer jwtConsumer = JwtConsumer.create("jwtConsumer");
			JwtToken access_Token =  jwtConsumer.createJwt(SessionUtils.getJwtToken());
			String name = access_Token.getClaims().getClaim("username", String.class);
      System.out.println("JWT  Consumer " + name);
		} catch (InvalidConsumerException | InvalidTokenException e1) {
			e1.printStackTrace();
			//e1.printStackTrace(out);
		}
  }

  private void buildJWT(HttpServletRequest request, String role) {
     try {
      JwtBuilder jwtBuilder = JwtBuilder.create();
      jwtBuilder.subject(username).claim(Claims.AUDIENCE, "http://localhost:9080/event/jwt").claim("iss","http://localhost:9080/jwtBuilder" ).claim("alg","RS256" ).claim("username", username).claim("password", password).claim("role", role);
      JwtToken goToken = jwtBuilder.buildJwt();
      String newJwt = goToken.compact();
      System.out.println("Writer Interceptor added token :: "+newJwt);

      // get the current session
      HttpSession ses = request.getSession(false);
      if (ses == null) {
        System.out.println("Session is timeout.");
      }
      ses.setAttribute("jwt", newJwt); // important to set it here!


    } catch (InvalidClaimException e) {
      System.out.println("InvalidClaimException");
      e.printStackTrace();
    } catch (JwtException e) {
      System.out.println("JwtException");
      e.printStackTrace();
    } catch (InvalidBuilderException e) {
      System.out.println("InvalidBuilderException");
      e.printStackTrace();
    }



  }


  private String getRole(HttpServletRequest request) {
    // to check if remote user is granted admin role
    boolean isAdmin = request.isUserInRole("eventAdministrator");
    if (isAdmin) {
      return "eventAdministrator";
    }
    return "registeredUser";
  }

  /**
   * updateSessionUser() : Update currently logged in user's info
   */
  private void updateSessionUser(HttpServletRequest request, String role) {

    // get the current session
    HttpSession ses = request.getSession(false);
    if (ses == null) {
      System.out.println("Session is timeout.");
    }

    User user = new User(username, "", password, role);
    ses.setAttribute("user", user); // important to set it here!

    UserList userlist = UserService.userList;
    User element = userlist.getUser(username);

    if (element == null) {
      userlist.addUser(user);
      System.out.println("created and added new login user " + username
          + " "+ password + " " + role);
    }
  }

  /**
   * readConfig() : Reads revoked user list file and creates a revoked user
   * list.
   */
  private List<String> readConfig() {
    List<String> list = new ArrayList<String>();
    String filename = "revokedUsers.lst";
    String filepath = System.getProperty("user.dir").split("target")[0] + filename;
    System.out.println(filepath);

    // get the revoked user list file and open it.
    BufferedReader in;
    try {
      in = new BufferedReader(new FileReader(filepath));
    } catch (FileNotFoundException fnfe) {
      return list;
    }
    // read all the revoked users and add to revokeList.
    String userName;
    try {
      while ((userName = in.readLine()) != null)
        list.add(userName);
    } catch (IOException ioe) {
      return list;
    }
    return list;
  }

  /* Sample code to retrieve LTPA token on the IBM WebSphere Liberty Profile */
  /* @return an LTPA token */
  public String getSecurityTokenLiberty() {
    Cookie cookie = null;
    String token = null;
    try {
      cookie = WebSecurityHelper.getSSOCookieFromSSOToken();
      if (cookie != null) {
        System.out.println(cookie.getName());
        token = cookie.getValue();
      }
    } catch (Exception e) {
      token = "no token found";
      e.printStackTrace();
    }
    return token;
  }

}
