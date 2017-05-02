package org.superbiz.moviefun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.misc.Contended;

import java.util.Map;

/**
 * Created by pivotal on 5/1/17.
 */

@Controller
public class HomeController {

   // @Autowired
    private MoviesBean moviesBean;
    HomeController(@Autowired MoviesBean moviesBean)
    {
        this.moviesBean  = moviesBean;
    }
    @RequestMapping("/")
    public String index()
    {

        return "index";
    }

    @Transactional
    @RequestMapping("/setup")
    public String setup(Map<String, Object> model) {

        moviesBean.addMovie(new Movie("Wedding Crashers", "David Dobkin", "Comedy", 7, 2005));
        moviesBean.addMovie(new Movie("Starsky & Hutch", "Todd Phillips", "Action", 6, 2004));
        moviesBean.addMovie(new Movie("Shanghai Knights", "David Dobkin", "Action", 6, 2003));
        moviesBean.addMovie(new Movie("I-Spy", "Betty Thomas", "Adventure", 5, 2002));
        moviesBean.addMovie(new Movie("The Royal Tenenbaums", "Wes Anderson", "Comedy", 8, 2001));
        moviesBean.addMovie(new Movie("Zoolander", "Ben Stiller", "Comedy", 6, 2001));
        moviesBean.addMovie(new Movie("Shanghai Noon", "Tom Dey", "Comedy", 7, 2000));

        model.put("movies", moviesBean.getMovies());
        return "setup";
    }

    @GetMapping("/moviefun")
    public String moviefun() {

        return "moviefun";
    }

}

