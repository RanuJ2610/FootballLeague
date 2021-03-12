package com.fl.service.impl;

import com.fl.service.config.app.FootballApiClient;
import com.fl.service.dto.LeagueDto;
import com.fl.service.dto.TeamDto;
import com.fl.service.exception.FootballException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FootBallLeagueService {

    private final FootballApiClient footballApiClient;

    public FootBallLeagueService(FootballApiClient footballApiClient) {
        this.footballApiClient = footballApiClient;
    }

    public Map<String,LeagueDto> getLeagueList(){
        Map<String, LeagueDto> leagues = new HashMap<>();
        try {
            List<LeagueDto> leagueDtos =  footballApiClient.getResourceAsList("get_leagues", LeagueDto.class);
            leagues = leagueDtos.stream()
                    .collect(Collectors.toMap(LeagueDto::getLeague_id,  leagueDto -> leagueDto));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leagues;
    }

    public List<TeamDto> getTeamList(String leagueId) throws FootballException{
        try {
            Map<String, LeagueDto> leagues = getLeagueList();
            if(leagues.containsKey(leagueId)) {
                LeagueDto leagueDto = leagues.get(leagueId);
                List<TeamDto> teamDtos =  footballApiClient.getResourceAsList("get_standings&league_id=" + leagueId, TeamDto.class);
                for(TeamDto teamDto: teamDtos){
                    teamDto.setCountry_id(leagueDto.getCountry_id());
                    teamDto.setCountry_name(leagueDto.getCountry_name());
                    teamDto.setLeague_id(leagueDto.getLeague_id());
                    teamDto.setLeague_name(teamDto.getTeam_name());
                }
                return teamDtos;
            }
            throw new FootballException("No League Data Found");
        } catch (FootballException e) {
           throw new FootballException(e.getMessage());
        }
    }
}
