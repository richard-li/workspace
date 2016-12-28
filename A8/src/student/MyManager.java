package student;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import game.Manager;
import game.Node;
import game.Parcel;
import game.Truck;

public class MyManager extends Manager {
	boolean preProcess;

	public MyManager() {
	}

	@Override
	public void run() {
		Node depot = getBoard().getTruckDepot();
		Set<Parcel> parcels = getParcels();
		ArrayList<Truck> trucks = getTrucks();
		ArrayList<Parcel> unmatchedParcels = new ArrayList<Parcel>();
		
		for (Truck t : trucks) {
			ArrayList<Parcel> data = new ArrayList<Parcel>();
			for (Parcel p : parcels) {
				if (t.getColor() == p.getColor()) {
					data.add(p);
					if (unmatchedParcels.contains(p))
						unmatchedParcels.remove(p);
				} else {
					unmatchedParcels.add(p);
				}
			}
			if (data.size() > 0)
				t.setUserData(data.get(0));
		}
		
		int n = 0;
		for (Truck t : trucks) {
			if (t.getUserData() == null && n < unmatchedParcels.size()) {
				t.setUserData(unmatchedParcels.get(n));
			}
			n++;	
		}
		
		for (Truck t : trucks) {
			Parcel parcel = (Parcel) t.getUserData();
			t.setTravelPath(Paths.dijkstra(depot, parcel.start));
		}
		preProcess = true;
	}

	@Override
	public void truckNotification(Truck t, Notification message) {
		if (!preProcess)
			return;
		
		Node now = t.getLocation();
		if (getParcels().size() > 0) {
			
			// parcel on truck
			if (t.getLoad() != null) {
				synchronized (getParcels()) {
					Node end = t.getLoad().destination;
					if (message == Notification.WAITING) {
						LinkedList<Node> path = Paths.dijkstra(now, end);
						t.setTravelPath(path);
					} else if (message == Notification.LOCATION_CHANGED && now == end) {
						t.dropoffLoad();
						t.setUserData(null);
					}
				}
			// parcel not on truck
			} else {
				synchronized (getParcels()) {
					if (t.getUserData() == null) {
						ArrayList<Parcel> avail = new ArrayList<Parcel>(getParcels());
						ArrayList<Parcel> nextParcel = new ArrayList<Parcel>();
						boolean found = false;
						for (Parcel p : avail) {
							if (t.getColor() == p.getColor()) {
								nextParcel.add(p);
								found = true;
							}
						}
						if (found) {
							t.setUserData(nextParcel.get(0));
						}
						if (!found) {
							t.setUserData(avail.get(0));
						}
					}
				}
				Parcel parcel = (Parcel) t.getUserData();
				if (message == Notification.WAITING) {
					t.setTravelPath(Paths.dijkstra(now, parcel.start));
				}
				if (message == Notification.PARCEL_AT_NODE && now == parcel.start) {
					t.pickupLoad(parcel);
				}
			}
		} else {
			LinkedList<Node> toHome = Paths.dijkstra(t.getLocation(), getBoard().getTruckDepot());
			t.setTravelPath(toHome);
		}
	}
}
