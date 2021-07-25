package com.laioffer.jupiter.servlet;

import com.laioffer.jupiter.entity.Game;
import com.fasterxml.jackson.databind.ObjectMapper;
import external.TwitchClient;
import external.TwitchException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "GameServlet", urlPatterns = "/game")
public class GameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String gameName = request.getParameter("game_name");
        TwitchClient client = new TwitchClient();
        // Let the client know the returned data is in JSON format.
        response.setContentType("application/json;charset=UTF-8");
        try {
            // if gameName is provided return the new Game obj,
            // otherwise return the top k games
            if (gameName == null) {
                response.getWriter().print(new ObjectMapper().writeValueAsString(client.topGames(0)));
            } else {
                response.getWriter().print(new ObjectMapper().writeValueAsString(client.searchGame(gameName)));
            }
        } catch (TwitchException e) {
            throw new ServletException(e); //  this will throw an internal error 500
        }
    }

}
