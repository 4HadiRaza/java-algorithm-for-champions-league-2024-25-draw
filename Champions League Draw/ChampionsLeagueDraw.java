import java.util.*;

public class ChampionsLeagueDraw {

    static class Team {
        String name;
        String country;

        Team(String name, String country) {
            this.name = name;
            this.country = country;
        }

        @Override
        public String toString() {
            return name + " (" + country + ")";
        }
    }

    static Map<Team, List<Team>> globalMatchups = new HashMap<>(); // Global match tracking

    public static void main(String[] args) {
        // Teams in each pot
        Team[] pot1 = {
            new Team("Real Madrid", "Spain"), new Team("Barcelona", "Spain"), new Team("Man City", "England"),
            new Team("PSG", "France"), new Team("RB Leipzig", "Germany"), new Team("Bayern Munich", "Germany"),
            new Team("Liverpool", "England"), new Team("Inter Milan", "Italy"), new Team("Dortmund", "Germany")
        };

        Team[] pot2 = {
            new Team("Arsenal", "England"), new Team("Leverkusen", "Germany"), new Team("Atletico", "Spain"),
            new Team("Atalanta", "Italy"), new Team("Juventus", "Italy"), new Team("Benfica", "Portugal"),
            new Team("Club Brugge", "Belgium"), new Team("Shakhtar", "Ukraine"), new Team("AC Milan", "Italy")
        };

        Team[] pot3 = {
            new Team("Dinamo", "Croatia"), new Team("PSV", "Netherlands"), new Team("Feyenoord", "Netherlands"),
            new Team("Sporting CP", "Portugal"), new Team("Salzburg", "Austria"), new Team("Lille", "France"),
            new Team("Redstar Belgrade", "Serbia"), new Team("Young Boys", "Switzerland"), new Team("Celtic", "Scotland")
        };

        Team[] pot4 = {
            new Team("Bratislava", "Slovakia"), new Team("Monaco", "France"), new Team("Sparta Praha", "Czech Republic"),
            new Team("Aston Villa", "England"), new Team("Bologna", "Italy"), new Team("Girona", "Spain"),
            new Team("Stuttgart", "Germany"), new Team("Strum Graz", "Austria"), new Team("Brest", "France")
        };

        // Display all teams
        System.out.println("Teams in Pot 1:");
        displayTeams(pot1);
        System.out.println("\nTeams in Pot 2:");
        displayTeams(pot2);
        System.out.println("\nTeams in Pot 3:");
        displayTeams(pot3);
        System.out.println("\nTeams in Pot 4:");
        displayTeams(pot4);

        // Generate and display matches for each team in Pot 1
        System.out.println("\nMatchups for each team in Pot 1:");
        for (Team team : pot1) {
            List<Team> matchups = generateMatchups(team, pot1, pot2, pot3, pot4);
            System.out.println(team + " will play against:");
            for (Team matchup : matchups) {
                System.out.println("  - " + matchup);
            }
            System.out.println();
        }
    }

    // Display teams in a pot
    private static void displayTeams(Team[] pot) {
        for (int i = 0; i < pot.length; i++) {
            System.out.println((i + 1) + ". " + pot[i]);
        }
    }

    // Generate matchups for a team: 2 from each pot (including own pot), avoiding same country and limiting to 2 teams per country
    private static List<Team> generateMatchups(Team team, Team[] pot1, Team[] pot2, Team[] pot3, Team[] pot4) {
        List<Team> matchups = new ArrayList<>();
        Map<String, Integer> countryCount = new HashMap<>();

        matchups.addAll(selectValidTeams(team, pot1, 2, countryCount, true)); // 2 from Pot 1 (including own pot)
        matchups.addAll(selectValidTeams(team, pot2, 2, countryCount, false));
        matchups.addAll(selectValidTeams(team, pot3, 2, countryCount, false));
        matchups.addAll(selectValidTeams(team, pot4, 2, countryCount, false));

        globalMatchups.put(team, matchups); // Track matchups globally
        return matchups;
    }

    // Select n valid teams from a given pot
    private static List<Team> selectValidTeams(Team team, Team[] pot, int n, Map<String, Integer> countryCount, boolean isSamePot) {
        List<Team> validTeams = new ArrayList<>();
        List<Team> shuffledPot = new ArrayList<>(Arrays.asList(pot));
        Collections.shuffle(shuffledPot);

        for (Team candidate : shuffledPot) {
            if (candidate == team && isSamePot) continue; // Skip the same team in its own pot
            if (!candidate.country.equals(team.country) && countryCount.getOrDefault(candidate.country, 0) < 2) {
                // Ensure reciprocal matchups
                if (globalMatchups.containsKey(candidate) && globalMatchups.get(candidate).contains(team)) {
                    validTeams.add(candidate);
                } else if (!globalMatchups.containsKey(candidate) || !globalMatchups.get(candidate).contains(team)) {
                    validTeams.add(candidate);
                    countryCount.put(candidate.country, countryCount.getOrDefault(candidate.country, 0) + 1);
                }
            }
            if (validTeams.size() == n) break;
        }

        return validTeams;
    }
}
