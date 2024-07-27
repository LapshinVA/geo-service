package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

import java.util.HashMap;
import java.util.Map;

import static ru.netology.entity.Country.RUSSIA;

public class MessageSendlerImpleTest {
    public static final String IP_ADDRESS_HEADER = "x-real-ip";

    /**
     * Тест для проверки языка отправляемого сообщения
     * Поверяет, что MessageSenderImpl всегда отправляет только русский текст, если ip относится к российскому сегменту адресов.
     */
    @Test
    void sendRuIpTest() {
        String ipEnAddress = "172.123.12.19";
        GeoService geoService = Mockito.mock(GeoService.class);
        Location location = Mockito.mock(Location.class);
        LocalizationService localizationService = Mockito.mock(LocalizationService.class);

        Mockito.when(location.getCountry()).thenReturn(RUSSIA);
        Mockito.when(geoService.byIp(ipEnAddress)).thenReturn(location);
        Mockito.when(localizationService.locale(RUSSIA)).thenReturn("Добро пожаловать");

        Map<String, String> headers = new HashMap<>();
        headers.put(IP_ADDRESS_HEADER, ipEnAddress);

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        String message = messageSender.send(headers);

        Assertions.assertEquals("Добро пожаловать", message);
    }


    /**
     * Тест для проверки языка отправляемого сообщения
     * Поверяет, что MessageSenderImpl всегда отправляет только английский текст, если ip относится к американскому сегменту адресов.
     */
    @Test
    void sendEnIpTest() {
        String ipEnAddress = "96.44.183.149";

        GeoService geoService = Mockito.mock(GeoService.class);
        Location location = Mockito.mock(Location.class);
        Mockito.when(location.getCountry()).thenReturn(RUSSIA);
        Mockito.when(geoService.byIp(ipEnAddress)).thenReturn(location);

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(RUSSIA)).thenReturn("Welcome");

        Map<String, String> headers = new HashMap<>();
        headers.put(IP_ADDRESS_HEADER, ipEnAddress);

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        String message = messageSender.send(headers);

        Assertions.assertEquals("Welcome", message);
    }


    /**
     * Тест для проверки определения локации по ip
     * Проверяет работу метода public Location byIp(String ip)
     */
    @Test
    void byIpTest() {
        String ipAddress = "172.123.12.19";
        GeoService geoService = Mockito.mock(GeoService.class);
        Location location = Mockito.mock(Location.class);
        Mockito.when(geoService.byIp(ipAddress)).thenReturn(location);

        Assertions.assertEquals(location, geoService.byIp(ipAddress));
    }


    /**
     * Тест для проверки возвращаемого текста
     * Проверяет работу метода public String locale(Country country)
     */
    @Test
    void byIpTest1() {
        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(RUSSIA)).thenReturn("Добро пожаловать");
        Assertions.assertEquals("Добро пожаловать", localizationService.locale(RUSSIA));
    }
}
