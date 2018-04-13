package ws.tilda.anastasia.biotopeapp.objects;

import java.util.List;

public class IotbnbResult {
    private List<AvailableService> mAvailableServices;

    public List<AvailableService> getAvailableServices() {
        return mAvailableServices;
    }

    public void setAvailableServices(List<AvailableService> availableServices) {
        mAvailableServices = availableServices;
    }

    public void addAvailableService(AvailableService availableService) {
        mAvailableServices.add(availableService);
    }
}
