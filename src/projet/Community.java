package projet;

import java.util.ArrayList;

public class Community {
	ArrayList<Ville> cities;
	ArrayList<Route> routes;

	public Community() {
		cities = new ArrayList<>();
		routes = new ArrayList<>();
	}

	public void addVille(String name) {
		Ville newVille = new Ville(name);
		if (cities.contains(newVille)) {
			System.out.println("Cette ville existe déjà.");
		} else {
			cities.add(newVille);
		}
	}

	public void addRoute(String city1, String city2) {
		Ville ville1 = findVille(city1);
		Ville ville2 = findVille(city2);

		if (ville1 != null && ville2 != null) {
			for (Route route : routes) {
				if ((route.getCity1().equals(ville1) && route.getCity2().equals(ville2))
						|| (route.getCity1().equals(ville2) && route.getCity2().equals(ville1))) {
					System.out.println("Cette route existe déjà.");
					return;
				}
			}
			routes.add(new Route(ville1, ville2));
			System.out.println("Route ajoutée entre les villes " + ville1.getName() + " et " + ville2.getName() + ".");
		} else {
			System.out.println("Les villes spécifiées dans la route ne sont pas valides.");
		}
	}

	public void addRechargeZone(String city) {
		Ville ville = findVille(city);
		if (ville != null) {
			if (!ville.hasZoneDeRecharge()) {
				ville.setZoneDeRecharge();
				System.out.println("Zone de recharge ajoutée à la ville " + city + ".");
			} else {
				System.out.println("Cette ville a déjà une zone de recharge.");
			}
		} else {
			System.out.println("La ville n'existe pas.");
		}
	}

	public void deleteRechargeZone(String city) {
		Ville ville = findVille(city);
		if(isAccessibilityPreserved(ville)) {
			if (ville != null) {
				if (ville.hasZoneDeRecharge()) {
					ville.deleteZoneDeRecharge();;
					System.out.println("Zone de recharge retirée de la ville " + city + ".");
				} else {
					System.out.println("Cette ville n'a pas une zone de recharge.");
				}
			} else {
				System.out.println("La ville n'existe pas.");
			}
		} else  {
			System.out.println("impossible de supprimer cette ville");
		}
		
	}

	public boolean isAccessibilityPreserved(){
		Ville city1, city2;
		for(Ville city : cities){
			if(!city.hasZoneDeRecharge()){
				for(Route route : routes){
					city1 = route.getCity1();
					city2 = route.getCity2();
					if((city1.equals(city) && !city2.hasZoneDeRecharge()) || (city2.equals(city) && !city1.hasZoneDeRecharge())) {
						return false;
					}
					
				}
			}
		}
		return true;
	}
	
	private boolean isAccessibilityPreserved(Ville city) {
		if(!city.hasZoneDeRecharge()){
			Ville city1, city2;
			for(Route route : routes){
				city1 = route.getCity1();
				city2 = route.getCity2();
				if((city1.equals(city) && !city2.hasZoneDeRecharge()) || (city2.equals(city) && !city1.hasZoneDeRecharge())) {
					return false;
				}
					
			}
		}
		return true;
	}



	public void displayRechargeZones() {
		System.out.println("Villes avec des zones de recharge :");
		for (Ville ville : cities) {
			if (ville.hasZoneDeRecharge()) {
				System.out.print(ville.getName() + " ");
			}
		}
		System.out.println();
	}

	private Ville findVille(String name) {
		for (Ville ville : cities) {
			if (ville.getName().equals(name)) {
				return ville;
			}
		}
		return null;
	}
	public void approximateSolution(int k) {
        int i = 0;
        int currentScore = getRechargeZonesCount();

        while (i < k) {
            Ville randomCity = getRandomCity();

            if (randomCity.hasZoneDeRecharge()) {
                randomCity.deleteZoneDeRecharge();
            } else {
                randomCity.setZoneDeRecharge();
            }

            if (getRechargeZonesCount() < currentScore) {
                i = 0;
                currentScore = getRechargeZonesCount();
            } else {
                i++;
            }
        }

        // Vérifier et corriger la contrainte à la fin de l'algorithme
        ensureAccessibility();
    }

    // ... (autres méthodes existantes)

    private void ensureAccessibility() {
        for (Ville city : cities) {
            if (!city.hasZoneDeRecharge()) {
                boolean hasAdjacentRechargeZone = false;

                for (Route route : routes) {
                    Ville city1 = route.getCity1();
                    Ville city2 = route.getCity2();

                    if (city.equals(city1) && city2.hasZoneDeRecharge()) {
                        hasAdjacentRechargeZone = true;
                        break;
                    } else if (city.equals(city2) && city1.hasZoneDeRecharge()) {
                        hasAdjacentRechargeZone = true;
                        break;
                    }
                }

                if (!hasAdjacentRechargeZone) {
                    city.setZoneDeRecharge();
                    System.out.println("Zone de recharge ajoutée à la ville " + city.getName() +
                            " pour garantir l'accessibilité.");
                }
            }
        }
    }

    private Ville getRandomCity() {
        int randomIndex = (int) (Math.random() * cities.size());
        return cities.get(randomIndex);
    }

    private int getRechargeZonesCount() {
        int count = 0;
        for (Ville ville : cities) {
            if (ville.hasZoneDeRecharge()) {
                count++;
            }
        }
        return count;
    }
}
