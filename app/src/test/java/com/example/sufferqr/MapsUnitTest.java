package com.example.sufferqr;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.android.gms.maps.model.LatLng;

@RunWith(MockitoJUnitRunner.class)
public class MapsUnitTest {

    private MapsActivity mapsActivity;

    @Mock
    private LatLng location1;

    @Mock
    private LatLng location2;

    @Before
    public void setUp() {
        mapsActivity = new MapsActivity();
    }

    @Test
    public void testIsWithinOneKilometer_sameLocation_returnsTrue() {
        // Given
        when(location1.latitude).thenReturn(12.9715987);
        when(location1.longitude).thenReturn(77.5945627);

        when(location2.latitude).thenReturn(12.9715987);
        when(location2.longitude).thenReturn(77.5945627);

        // When
        boolean result = mapsActivity.isWithinOneKilometer(location1, location2);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsWithinOneKilometer_withinOneKilometer_returnsTrue() {
        // Given
        when(location1.latitude).thenReturn(12.9715987);
        when(location1.longitude).thenReturn(77.5945627);

        when(location2.latitude).thenReturn(12.977396);
        when(location2.longitude).thenReturn(77.590063);

        // When
        boolean result = mapsActivity.isWithinOneKilometer(location1, location2);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsWithinOneKilometer_outsideOneKilometer_returnsFalse() {
        // Given
        when(location1.latitude).thenReturn(12.9715987);
        when(location1.longitude).thenReturn(77.5945627);

        when(location2.latitude).thenReturn(12.989643);
        when(location2.longitude).thenReturn(77.577956);

        // When
        boolean result = mapsActivity.isWithinOneKilometer(location1, location2);

        // Then
        assertFalse(result);
    }
}
