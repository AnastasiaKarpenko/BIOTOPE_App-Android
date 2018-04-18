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
import ws.tilda.anastasia.biotopeapp.objects.AvailableService;
import ws.tilda.anastasia.biotopeapp.objects.Charger;
import ws.tilda.anastasia.biotopeapp.objects.GeoCoordinates;
import ws.tilda.anastasia.biotopeapp.objects.IotbnbResult;
import ws.tilda.anastasia.biotopeapp.objects.OpeningHoursSpecifications;
import ws.tilda.anastasia.biotopeapp.objects.ParkingCapacity;
import ws.tilda.anastasia.biotopeapp.objects.ParkingFacility;
import ws.tilda.anastasia.biotopeapp.objects.ParkingService;
import ws.tilda.anastasia.biotopeapp.objects.ParkingSpace;
import ws.tilda.anastasia.biotopeapp.objects.Plug;


public class IotbnbParser {
    private IotbnbResult result;

    public IotbnbResult parse(InputStream is) throws XmlPullParserException, IOException, IllegalStateException {
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
                    result = parseObjectsElement(objectElement);
                }
            }

        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }


        return result;
    }


    private IotbnbResult parseObjectsElement(Element element) {
        IotbnbResult result = new IotbnbResult();
        NodeList objects = element.getChildNodes();
        for (int i = 0; i < objects.getLength(); i++) {
            Node object = objects.item(i);

            if (object.getNodeType() == Node.ELEMENT_NODE) {
                Element objectElement = (Element) object;
                result = parseObjectResult(objectElement);
            }
        }
        return result;
    }

    private IotbnbResult parseObjectResult(Element element) {
        IotbnbResult result = new IotbnbResult();
        List<AvailableService> availableServices = new ArrayList<>();
        result.setAvailableServices(availableServices);

        NodeList objects = element.getChildNodes();
        for (int i = 0; i < objects.getLength(); i++) { // id, object(list)
            Node object = objects.item(i);

            if (object.getNodeType() == Node.ELEMENT_NODE) {
                Element objectElement = (Element) object;

                if (objectElement.getTagName().equals("Object")) {
                    AvailableService availableService = parseAvailableService(objectElement);
                    availableServices.add(availableService);

                }
            }
        }
        return result;
    }

    private AvailableService parseAvailableService(Element nodeElement) {
        AvailableService availableService = new AvailableService();

        NodeList nodes = nodeElement.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) { // id, object(list)
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (element.getTagName().equals("id")) {
                    String id = element.getFirstChild().getTextContent();
                    availableService.setId(id);
                } else if (element.getTagName().equals("InfoItem")) {
                    if (element.getAttribute("name").equals("serviceID")) {
                        String serviceID = getValue("value", element);
                        availableService.setServiceID(serviceID);
                    } else if (element.getAttribute("name").equals("type")) {
                        String type = getValue("value", element);
                        availableService.setType(type);
                    } else if (element.getAttribute("name").equals("title")) {
                        String title = getValue("value", element);
                        availableService.setTitle(title);
                    } else if (element.getAttribute("name").equals("price")) {
                        Double price = Double.parseDouble(getValue("value", element));
                        availableService.setPrice(price);
                    } else if (element.getAttribute("name").equals("reputation")) {
                        Double reputation = Double.parseDouble(getValue("value", element));
                        availableService.setReputation(reputation);
                    } else if (element.getAttribute("name").equals("InfoItem_url")) {
                        String infoItemUrl = getValue("value", element);
                        availableService.setInfoitemUrl(infoItemUrl);
                    } else if (element.getAttribute("name").equals("OMI_node_url")) {
                        String omiNodeUrl = getValue("value", element);
                        availableService.setOmiNodeUrl(omiNodeUrl);
                    }
                } else if (element.getTagName().equals("Object")) {
                    if (element.getAttribute("type").equals("schema:GeoCoordinates")) {
                        GeoCoordinates position = parseGeoCoordinates(element);
                        availableService.setGeoCoordinates(position);
                    }
                }
            }
        }
        return availableService;
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
                }
            }
        }
        return position;
    }


    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

}

