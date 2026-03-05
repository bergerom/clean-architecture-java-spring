package core.ports.driving;

import core.entities.User;
import core.export.dto.TotalScoreDTO;

import java.util.List;

public interface GlobalScoreBoard {
    GlobalScoreBoardResponse getGlobalScoreBoard(GlobalScoreBoardRequest request);

    record GlobalScoreBoardResponse(List<TotalScoreDTO> totalScore) {}

    record GlobalScoreBoardRequest() {}

}
