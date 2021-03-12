package com.fl.service.controller;

import com.fl.service.dto.TeamDto;
import com.fl.service.impl.FootBallLeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/football")
public class FootballController {

    @Autowired
    private FootBallLeagueService footBallLeagueService;

    @GetMapping
    public ResponseEntity<List<TeamDto>> getFootballTeamInfo(String leagueId) {
        List<TeamDto> countryDtos = footBallLeagueService.getTeamList(leagueId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(countryDtos);
    }
}

