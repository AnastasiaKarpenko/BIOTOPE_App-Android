package ws.tilda.anastasia.biotopeapp.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ws.tilda.anastasia.biotopeapp.objects.Address;
import ws.tilda.anastasia.biotopeapp.objects.ParkingSpace;
import ws.tilda.anastasia.biotopeapp.objects.Charger;
import ws.tilda.anastasia.biotopeapp.objects.GeoCoordinates;
import ws.tilda.anastasia.biotopeapp.objects.OpeningHoursSpecifications;
import ws.tilda.anastasia.biotopeapp.objects.ParkingCapacity;
import ws.tilda.anastasia.biotopeapp.objects.ParkingFacility;
import ws.tilda.anastasia.biotopeapp.objects.ParkingService;
import ws.tilda.anastasia.biotopeapp.objects.Plug;


public class XmlParser {
    private ParkingService parkingService = new ParkingService();

    public ParkingService parse(InputStream is) throws XmlPullParserException, IOException, IllegalStateException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList objects = doc.getElementsByTagName("Objects");

            if (objects.getLength() > 0) {
                Node object = objects.item(0);
                if (object.getNodeType() == Node.ELEMENT_NODE) {
                    Element objectElement = (Element) object;
                    parkingService = parseObjectsElement(objectElement);
                }
            }

        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }


        return parkingService;
    }

    public String parseReturnCode(InputStream is) throws XmlPullParserException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        String returnCode = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList objects = doc.getElementsByTagName("return");

            if (objects.getLength() > 0) {
                Node object = objects.item(0);
                if (object.getNodeType() == Node.ELEMENT_NODE) {
                    Element objectElement = (Element) object;
                    returnCode = objectElement.getAttribute("returnCode");

                }
            }
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return returnCode;
    }

    public String parseReturnDescription(InputStream is) throws XmlPullParserException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        String returnDescription = null;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList objects = doc.getElementsByTagName("return");

            if (objects.getLength() > 0) {
                Node object = objects.item(0);
                if (object.getNodeType() == Node.ELEMENT_NODE) {
                    Element objectElement = (Element) object;
                    returnDescription = objectElement.getAttribute("description");

                }
            }
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return returnDescription;
    }

    private ParkingService parseObjectsElement(Element element) {
        ParkingService pService = new ParkingService();
        NodeList objects = element.getChildNodes();
        for (int i = 0; i < objects.getLength(); i++) {
            Node object = objects.item(i);

            if (object.getNodeType() == Node.ELEMENT_NODE) {
                Element objectElement = (Element) object;
                pService = parseObjectParkingService(objectElement);
            }
        }
        return pService;
    }



    private ParkingService parseObjectParkingService(Element element) {
        ParkingService parkingService = new ParkingService();

        NodeList objects = element.getChildNodes();
        for (int i = 0; i < objects.getLength(); i++) { // id, object(list)
            Node object = objects.item(i);

            if (object.getNodeType() == Node.ELEMENT_NODE) {
                Element objectElement = (Element) object;

                if (objectElement.getTagName().equals("Object")) {
                    List<ParkingFacility> parkingFacilities = parseParkingFacilityList(objectElement);
                    parkingService.setParkingFacilities(parkingFacilities);
                }
            }
        }
        return parkingService;
    }

    private List<ParkingFacility> parseParkingFacilityList(Element objectElement) {
        List<ParkingFacility> parkingLots = new ArrayList<>();
        NodeList nodes = objectElement.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element nodeElement = (Element) node;

                if (nodeElement.getTagName().equals("Object")) {
                    if (nodeElement.getAttribute("type").equals("schema:ParkingFacility")) {
                        ParkingFacility parkingFacility = parseParkingFacility(nodeElement);
                        parkingLots.add(parkingFacility);
                    }
                }
            }
        }
        return parkingLots;
    }

    private ParkingFacility parseParkingFacility(Element nodeElement) {
        ParkingFacility parkingLot = new ParkingFacility();

        NodeList nodes = nodeElement.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) { // id, object(list)
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (element.getTagName().equals("id")) {
                    String id = element.getFirstChild().getTextContent();
                    parkingLot.setId(id);
                } else if (element.getTagName().equals("InfoItem")) {
                    if (element.getAttribute("name").equals("ownedBy")) {
                        String owner = getValue("value", element);
                        parkingLot.setIsOwnedBy(owner);
                    }
                } else if (element.getTagName().equals("Object")) {
                    if (element.getAttribute("type").equals("schema:GeoCoordinates")) {
                        GeoCoordinates position = parseGeoCoordinates(element);
                        parkingLot.setGeoCoordinates(position);
                    } else if (element.getAttribute("type").equals("schema:OpeningHoursSpecification")) {
                        OpeningHoursSpecifications openingHours = parseOpeningHoursSpecification(element);
                        parkingLot.setOpeningHoursSpecifications(openingHours);
                    } else if (element.getAttribute("type").equals("list")) {
                        String id = element.getFirstChild().getTextContent();
                        if (id.equals("ParkingSpaces")) {
                            parkingLot.setParkingSpaces(parseParkingSpacesList(element));
                        } else if (id.equals("Capacities")) {
                            parkingLot.setCapacities(parseCapacities(element));
                        }
                    }
                }
            }
        }
        return parkingLot;
    }


    private GeoCoordinates parseGeoCoordinates(Element objectElement) {
        GeoCoordinates position = new GeoCoordinates();

        NodeList nodes = objectElement.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element nodeElement = (Element) node;

                if (nodeElement.getTagName().equals("id")) {
                    continue;
                } else if (nodeElement.getTagName().equals("InfoItem")) {
                    if (nodeElement.getAttribute("name").equals("longitude")) {
                        String lon = getValue("value", nodeElement);
                        double longitude = Double.parseDouble(lon);
                        position.setLongitude(longitude);
                    } else if (nodeElement.getAttribute("name").equals("latitude")) {
                        String lat = getValue("value", nodeElement);
                        double latitude = Double.parseDouble(lat);
                        position.setLatitude(latitude);
                    }
                } else if (nodeElement.getTagName().equals("Object")) {
                    if (nodeElement.getAttribute("type").equals("schema:PostalAddress")) {
                        Address address = parseAddress(nodeElement);
                        position.setAddress(address);
                    }
                }
            }
        }
        return position;
    }

    private Address parseAddress(Element objectElement) {
        Address address = new Address();
        NodeList nodes = objectElement.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element nodeElement = (Element) node;

                if (nodeElement.getTagName().equals("id")) {
                    continue;
                } else if (nodeElement.getTagName().equals("InfoItem")) {
                    if (nodeElement.getAttribute("name").equals("addressCountry")) {
                        String country = getValue("value", nodeElement);
                        address.setAddressCountry(country);
                    } else if (nodeElement.getAttribute("name").equals("streetAddress")) {
                        String street = getValue("value", nodeElement);
                        address.setStreetAddress(street);
                    } else if (nodeElement.getAttribute("name").equals("addressRegion")) {
                        String region = getValue("value", nodeElement);
                        address.setAddressRegion(region);
                    } else if (nodeElement.getAttribute("name").equals("addressLocality")) {
                        String locality = getValue("value", nodeElement);
                        address.setAddressLocality(locality);
                    } else if (nodeElement.getAttribute("name").equals("postalCode")) {
                        String postalCode = getValue("value", nodeElement);
                        address.setPostalCode(postalCode);
                    }
                }
            }
        }

        return address;
    }

    private OpeningHoursSpecifications parseOpeningHoursSpecification(Element objectElement) {
        OpeningHoursSpecifications openingHours = new OpeningHoursSpecifications();

        NodeList nodes = objectElement.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element nodeElement = (Element) node;

                if (nodeElement.getTagName().equals("id")) {
                    continue;
                } else if (nodeElement.getTagName().equals("InfoItem")) {
                    if (nodeElement.getAttribute("name").equals("closes")) {
                        String close = getValue("value", nodeElement);
                        openingHours.setCloses(close);
                    } else if (nodeElement.getAttribute("name").equals("opens")) {
                        String open = getValue("value", nodeElement);
                        openingHours.setOpens(open);
                    } else if (nodeElement.getAttribute("name").equals("dayOfWeek")) {
                        String dayOfWeek = getValue("value", nodeElement);
                        openingHours.setDayOfWeek(dayOfWeek);
                    }
                }
            }
        }
        return openingHours;
    }

    private List<ParkingCapacity> parseCapacities(Element objectElement) {
        List<ParkingCapacity> capacities = new ArrayList<>();
        NodeList nodes = objectElement.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element nodeElement = (Element) node;

                if (nodeElement.getTagName().equals("Object")) {
                    if (nodeElement.getAttribute("type").equals("mv:Capacity")) {
                        ParkingCapacity capacity = parseCapacity(nodeElement);
                        capacities.add(capacity);
                    }
                }
            }
        }
        return capacities;
    }

    private ParkingCapacity parseCapacity(Element nodeElement) {
        ParkingCapacity capacity = new ParkingCapacity();

        NodeList nodes = nodeElement.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (element.getTagName().equals("id")) {
                    String id = element.getFirstChild().getTextContent();
                    capacity.setId(id);
                } else if (element.getTagName().equals("InfoItem")) {
                    if (element.getAttribute("name").equals("realTimeValue")) {
                        int realTimeValue = Integer.valueOf(getValue("value", element));
                        capacity.setRealTimeValue(realTimeValue);
                    } else if (element.getAttribute("name").equals("maximumValue")) {
                        int maximumValue = Integer.valueOf(getValue("value", element));
                        capacity.setMaximumValue(maximumValue);
                    } else if (element.getAttribute("name").equals("validForVehicle")) {
                        String validForVehicle = getValue("value", element);
                        capacity.setValidForVehicle(validForVehicle);
                    } else if (element.getAttribute("name").equals("validForUserGroup")) {
                        String validForUserGroup = getValue("value", element);
                        if (validForUserGroup.equals("mv:PersonWithDisabledParkingPermit")) {
                            capacity.setValidForDisabled(true);
                        }
                    }
                }
            }
        }
        return capacity;
    }

    private List<ParkingSpace> parseParkingSpacesList(Element objectElement) {
        List<ParkingSpace> parkingSpaces = new ArrayList<>();
        NodeList nodes = objectElement.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element nodeElement = (Element) node;

                if (nodeElement.getTagName().equals("Object")) {
                    if (nodeElement.getAttribute("type").equals("mv:ParkingSpace")) {
                        ParkingSpace parkingSpace = parseParkingSpace(nodeElement);
                        parkingSpaces.add(parkingSpace);
                    }
                }
            }
        }
        return parkingSpaces;
    }

    private ParkingSpace parseParkingSpace(Element nodeElement) {
        ParkingSpace parkingSpace = new ParkingSpace();

        NodeList nodes = nodeElement.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (element.getTagName().equals("id")) {
                    String id = element.getFirstChild().getTextContent();
                    parkingSpace.setId(id);
                } else if (element.getTagName().equals("InfoItem")) {
                    if (element.getAttribute("name").equals("available")) {
                        String available = getValue("value", element).toLowerCase();
                        boolean isAvailable = Boolean.valueOf(available);
                        parkingSpace.setAvailable(isAvailable);
                    } else if (element.getAttribute("name").equals("validForVehicle")) {
                        String validForVehicle = getValue("value", element);
                        parkingSpace.setValidForVehicle(validForVehicle);
                        if (validForVehicle.equals("ElectricVehicle")) {
                            parkingSpace.setHasEvCharging(true);
                        }
                    } else if (element.getAttribute("name").equals("validForUserGroup")) {
                        String validForUserGroup = getValue("value", element);
                        if (validForUserGroup.equals("mv:PersonWithDisabledParkingPermi")) {
                            parkingSpace.setCapableForDisabled(true);
                        }
                    } else if (element.getAttribute("name").equals("vechileHeightLimit")) {
                        double vehicleHeightLimit = Double.parseDouble(getValue("value", element));
                        parkingSpace.setVehicleHeightLimit(vehicleHeightLimit);
                    } else if (element.getAttribute("name").equals("vechileWidthLimit")) {
                        double vehicleWidthLimit = Double.parseDouble(getValue("value", element));
                        parkingSpace.setVehicleWidthLimit(vehicleWidthLimit);
                    } else if (element.getAttribute("name").equals("vechileLengthLimit")) {
                        double vehicleLengthLimit = Double.parseDouble(getValue("value", element));
                        parkingSpace.setVehicleLengthLimit(vehicleLengthLimit);
                    }
                } else if (element.getAttribute("type").equals("mv:Charger")) {
                    parkingSpace.setHasEvCharging(true);
                    Charger charger = parseCharger(element);
                    parkingSpace.setCharger(charger);

                }
            }
        }

        return parkingSpace;
    }

    private Charger parseCharger(Element nodeElement) {
        Charger charger = new Charger();

        NodeList nodes = nodeElement.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (element.getTagName().equals("id")) {
                    String id = element.getFirstChild().getTextContent();
                    charger.setId(id);
                } else if (element.getTagName().equals("InfoItem")) {
                    if (element.getAttribute("name").equals("model")) {
                        String model = getValue("value", element).toLowerCase();
                        charger.setModel(model);
                    } else if (element.getAttribute("name").equals("brand")) {
                        String brand = getValue("value", element);
                        charger.setBrand(brand);
                    } else if (element.getAttribute("name").equals("available")) {
                        String isAvailable = getValue("value", element);
                        charger.setAvailable(Boolean.parseBoolean(isAvailable));
                    } else if (element.getAttribute("name").equals("isFastChargeCapable")) {
                        boolean isFastChargeCapable = Boolean.valueOf(getValue("value", element));
                        charger.setFastChargeCapable(isFastChargeCapable);
                    } else if (element.getAttribute("name").equals("three-phasedCurrentAvailable")
                            || element.getAttribute("name").equals("threePhasedCurrentAvailable")) {
                        boolean threePhasedCurrentAvailable = Boolean.valueOf(getValue("value", element));
                        charger.setThreePhasedCurrentAvailable(threePhasedCurrentAvailable);
                    } else if (element.getAttribute("name").equals("currentInA")) {
                        double currentInA = Double.valueOf(getValue("value", element));
                        charger.setCurrentInA(currentInA);
                    } else if (element.getAttribute("name").equals("powerInkW")) {
                        double powerInkW = Double.valueOf(getValue("value", element));
                        charger.setPowerInKW(powerInkW);
                    } else if (element.getAttribute("name").equals("voltageInV")) {
                        double voltageInV = Double.valueOf(getValue("value", element));
                        charger.setVoltageInV(voltageInV);
                    } else if (element.getAttribute("name").equals("currentType")) {
                        String currentType = getValue("value", element);
                        charger.setCurrentType(currentType);
                    }
                } else if (element.getTagName().equals("Object")) {
                    if (element.getAttribute("type").equals("list")) {
                        String id = element.getFirstChild().getTextContent();
                        if (id.equals("Plugs")) {
                            charger.setPlugs(parsePlugList(element));
                        }
                    }
                }
            }
        }
        return charger;
    }

    private List<Plug> parsePlugList(Element objectElement) {
        List<Plug> plugs = new ArrayList<>();
        NodeList nodes = objectElement.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element nodeElement = (Element) node;

                if (nodeElement.getTagName().equals("Object")) {
                    if (nodeElement.getAttribute("type").equals("mv:Plug")) {
                        Plug plug = parsePlug(nodeElement);
                        plugs.add(plug);
                    }
                }
            }
        }
        return plugs;
    }

    private Plug parsePlug(Element nodeElement) {
        Plug plug = new Plug();

        NodeList nodes = nodeElement.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (element.getTagName().equals("id")) {
                    String id = element.getFirstChild().getTextContent();
                    plug.setId(id);
                } else if (element.getTagName().equals("InfoItem")) {
                    if (element.getAttribute("name").equals("isFastChargeCapable")) {
                        boolean isFastChargeCapable = Boolean.valueOf(getValue("value", element));
                        plug.setFastChargeCapable(isFastChargeCapable);
                    } else if (element.getAttribute("name").equals("three-phasedCurrentAvailable")
                            || element.getAttribute("name").equals("threePhasedCurrentAvailable")) {
                        boolean threePhasedCurrentAvailable = Boolean.valueOf(getValue("value", element));
                        plug.setThreePhasedCurrentAvailable(threePhasedCurrentAvailable);
                    } else if (element.getAttribute("name").equals("currentInA")) {
                        double currentInA = Double.valueOf(getValue("value", element));
                        plug.setCurrentInA(currentInA);
                    } else if (element.getAttribute("name").equals("powerInkW")) {
                        double powerInkW = Double.valueOf(getValue("value", element));
                        plug.setPowerInKW(powerInkW);
                    } else if (element.getAttribute("name").equals("voltageInV")) {
                        double voltageInV = Double.valueOf(getValue("value", element));
                        plug.setVoltageInV(voltageInV);
                    } else if (element.getAttribute("name").equals("currentType")) {
                        String currentType = getValue("value", element);
                        plug.setCurrentType(currentType);
                    } else if (element.getAttribute("name").equals("plugType")) {
                        String plugType = getValue("value", element);
                        plug.setPlugType(plugType);
                    }
                }
            }
        }
        return plug;

    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

}

