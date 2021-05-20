package com.publicis.footballleague.controller;


import com.publicis.footballleague.service.FootBallLeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/api")
public class FootBallLeagueController {

    @Autowired
    private FootBallLeagueService footBallLeagueService;

    @RequestMapping("/footBallLeague")
    public ResponseEntity<String> getOverAllLeaguePositionByCountryLeagueAndTeamName(@RequestParam("countryName") String countryName, @RequestParam("leagueName") String leagueName, @RequestParam("teamName")String teamName) {
        String overAllLeaguePosition = footBallLeagueService.getOverAllLeaguePositionByCountryLeagueAndTeamName(countryName,leagueName,teamName);
        return new ResponseEntity<String>(overAllLeaguePosition,HttpStatus.OK);
    }
}
