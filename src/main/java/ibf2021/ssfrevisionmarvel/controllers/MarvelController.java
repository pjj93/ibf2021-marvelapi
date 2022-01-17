package ibf2021.ssfrevisionmarvel.controllers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ibf2021.ssfrevisionmarvel.SsfRevisionMarvelApplication;
import ibf2021.ssfrevisionmarvel.models.MarvelCharacter;
import ibf2021.ssfrevisionmarvel.services.MarvelService;

@Controller
@RequestMapping
public class MarvelController {
    private final Logger logger = Logger.getLogger(SsfRevisionMarvelApplication.class.getName());
    
    @Autowired
    private MarvelService marvelSvc;

    @GetMapping(path="/characters")
    public String getMarvelCharacter (@RequestParam String nameStartsWith, Model model) {
        List<MarvelCharacter> marvelCharList = marvelSvc.getCharacters(nameStartsWith);
        logger.log(Level.INFO, "size: " + marvelCharList.size());
        if (marvelCharList.size() > 0) {
            model.addAttribute("marvelCharacters", marvelCharList);
            logger.log(Level.INFO, "added character to Model");
        }
        
        return "characters";
    }
}