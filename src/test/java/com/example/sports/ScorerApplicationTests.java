package com.example.sports;

import com.example.sports.service.MatchServiceIF;
import com.example.sports.service.ScoreServiceIF;
import com.example.sports.service.data.entity.MatchSet;
import com.example.sports.service.data.entity.Score;
import com.example.sports.service.data.entity.TennisMatch;
import com.example.sports.service.data.repository.MatchRepository;
import com.example.sports.service.data.repository.SetRepository;
import com.example.sports.service.exception.BadRequestException;
import com.example.sports.service.exception.MatchAlreadyConcludedException;
import com.example.sports.service.exception.NotFoundException;
import com.example.sports.tennis.controller.request.CreateMatchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = SportsApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ScorerApplicationTests {
	public static final String PLAYER1 = "A";
	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private ScoreServiceIF scoreServiceIF;

	@MockBean
	private MatchServiceIF matchServiceIF;

	@MockBean
	private SetRepository setRepository;

	@MockBean
	private MatchRepository matchRepository;

	@LocalServerPort
	private int port;

	@Autowired
	MockMvc mockMvc;

	@Test
	public void contextLoads() { }

	@Test
	public void testCreateMatch() throws BadRequestException {
		TennisMatch tennis = getFakeMatch();
		final CreateMatchRequest request = new CreateMatchRequest();
		request.setPlayer1(PLAYER1);
		request.setPlayer2("B");
		Mockito.when(matchServiceIF.createMatch(request)).thenReturn(tennis);

		try {
			mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/sports/tennis/matches")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(Optional.of(tennis))));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Assert.assertEquals(PLAYER1, tennis.getPlayer1());
	}

	@Test
	public void testScoreToPlayer() throws BadRequestException, NotFoundException, MatchAlreadyConcludedException {
		final MatchSet matchSet = getFakeSet();
		Map<String, String> mapResponse = new HashMap<>();
		mapResponse.put("scoreboard", "0 - 0, 0 - 15");
		Mockito.when(scoreServiceIF.pointWonBy(1l, PLAYER1)).thenReturn(matchSet);
		Mockito.when(scoreServiceIF.getScores(1l)).thenReturn(mapResponse);

		try {
			mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/sports/tennis/scores/1")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(matchSet)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map <String, String> m = scoreServiceIF.getScores(1l);
		Assert.assertEquals(Score.FIFTEEN, matchSet.getScore());
		Assert.assertEquals("0 - 0, 0 - 15", m.get("scoreboard"));
	}
	TennisMatch getFakeMatch() {
		return new TennisMatch(PLAYER1, "B");
	}

	MatchSet getFakeSet() {
		MatchSet matchSet = new MatchSet();
		matchSet.setScore(Score.FIFTEEN);
		matchSet.setPointsWonBy(PLAYER1);
		matchSet.setTennisMatch(getFakeMatch());

		return matchSet;
	}


	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
