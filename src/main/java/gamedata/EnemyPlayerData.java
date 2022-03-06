package gamedata;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class EnemyPlayerData {
    private Map<Coordinates, City> visibleEnemyCities;
    private Map<Coordinates, Hero> visibleEnemyHeroes;
}
