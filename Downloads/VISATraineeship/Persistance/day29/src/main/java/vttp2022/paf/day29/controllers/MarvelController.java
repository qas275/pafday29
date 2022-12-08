package vttp2022.paf.day29.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.paf.day29.model.Character;
import vttp2022.paf.day29.repositories.MarvelCache;
import vttp2022.paf.day29.services.MarvelService;

@Controller
public class MarvelController {
    
    @Autowired
    private MarvelService marvelSvc;

    @Autowired
    private MarvelCache marvelCache;

    @GetMapping(path = "/search")
    public String searchCharacter(@RequestParam String character, Model model){
        List<Character> res = null;
        Optional<List<Character>>opt = marvelCache.get(character);
        if(opt.isEmpty()){
            res = marvelSvc.search(character);
            marvelCache.cache(character, res);
        }else{
            res = opt.get();
            System.out.println("FROM CACHE>>>>>>>>>>>>>>>>\n");
        }
        model.addAttribute("characters", res);
        return "results";
    }
}
