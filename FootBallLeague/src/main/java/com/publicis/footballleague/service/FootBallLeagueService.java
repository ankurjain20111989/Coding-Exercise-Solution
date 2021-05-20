package com.publicis.footballleague.service;


import com.publicis.footballleague.model.Country;
import com.publicis.footballleague.model.League;
import com.publicis.footballleague.model.Standing;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FootBallLeagueService {

    private RestTemplate restTemplate;

    private String URI_GET = "https://apiv2.apifootball.com";

    private  String  API_KEY = "9bb66184e0c8145384fd2cc0f7b914ada57b4e8fd2e4d6d586adcc27c257a978";

    public String getOverAllLeaguePositionByCountryLeagueAndTeamName(String leagueName, String teamName,String countryName){
        Map<String,String> uriVariables = new HashMap<>();
        String overAllLeaguePosition = null;
        uriVariables.put("action","get_standings");
        uriVariables.put("league_id",getLeagueIdForCountryAndTeamName(countryName,leagueName));
        uriVariables.put("APIkey",API_KEY);
        ResponseEntity<List<Standing>> result = restTemplate.exchange(URI_GET, HttpMethod.GET, null, new ParameterizedTypeReference<List<Standing>>(){}, uriVariables);
        List<Standing> standings = result.getBody();
        List<Standing> filteredStandings = standings.stream().filter(s->s.getTeam_name().equals(teamName) && s.getLeague_name().equals(leagueName) && s.getCountry_name().equals(countryName)).collect(Collectors.toList());
        if(filteredStandings.size()<2){
            for(Standing standing: filteredStandings) {
                overAllLeaguePosition =  standing.getOverall_league_position();
            }
        }
        return overAllLeaguePosition;
    }

    private String getLeagueIdForCountryAndTeamName(String countryName, String leagueName) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("action", "get_leagues");
        List<Country> countries = getAllCountries();
        String countryId = null;
        List<Country> filteredCountries = countries.stream().filter(c -> c.getCountry_name().equals(countryName)).collect(Collectors.toList());
        if(filteredCountries.size()<2) {
            for (Country country : filteredCountries) {
                countryId = country.getCountry_id();
            }
        }
        uriVariables.put("country_id",countryId);
        uriVariables.put("APIkey", API_KEY);
        ResponseEntity<List<League>> resultLeagues = restTemplate.exchange(URI_GET, HttpMethod.GET, null, new ParameterizedTypeReference<List<League>>() {}, uriVariables);
        List<League> leagues = resultLeagues.getBody();
        List<League> filteredLeagues = leagues.stream().filter(l->l.getLeague_name().equals(leagueName) && l.getCountry_name().equals(countryName)).collect(Collectors.toList());
        String leagueId = null;
        if(filteredLeagues.size()<2) {
            for (League league : filteredLeagues) {
                leagueId = league.getLeague_id();
            }
        }
        return leagueId;
    }

    public List<Country> getAllCountries() {
        Map<String,String> uriVariables = new HashMap<>();
        uriVariables.put("action","get_countries");
        uriVariables.put("APIkey",API_KEY);
        ResponseEntity<List<Country>> resultCountries = restTemplate.exchange(URI_GET,HttpMethod.GET,null,new ParameterizedTypeReference<List<Country>>(){},uriVariables);
        List<Country> countries = resultCountries.getBody();
        return countries;
    }


    @Bean
    public RestTemplate restTemplate() {
        return restTemplate;
    }
}
