package gamedata;

import lombok.Data;

import java.util.List;

@Data
public class CoraksData {
    private List<City> cities;
    private List<Hero> heroes;
    private List<Resources> resources;
}
