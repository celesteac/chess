package requestresponsetypes;

import java.util.ArrayList;

public record ListResponse(ArrayList<GameDetails> games) {
}
