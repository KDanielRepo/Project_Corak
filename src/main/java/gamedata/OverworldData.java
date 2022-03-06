package gamedata;

import java.util.Map;

public class OverworldData {
    private MapSize mapSize;
    private Map<Coordinates, Resources> visibleResources;
    private Map<Coordinates, Unit> visibleUnits;
    private Map<Coordinates, Hero> ownedHeroes;
    private Map<Coordinates, Building> visibleOverworldBuildings;
    private Map<Coordinates, Artifacts> visibleOverworldArtifacts;
    private Map<Coordinates, City> visibleCities;
    private Map<Coordinates, City> ownedCities;
}
