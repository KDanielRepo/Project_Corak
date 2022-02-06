package gamedata;

import lombok.Data;

import java.util.List;

@Data
public class EnemyPlayerData {
    private List<City> cities;
    private List<Hero> heroes;
    private List<Resources> resources;
}
