/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.vrem.wifianalyzer.wifi.model;

import org.junit.Test;

import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FrequencyTest {
    @Test
    public void testFrequency() throws Exception {
        assertEquals(4, Frequency.values().length);
    }

    @Test
    public void testInRange() throws Exception {
        assertTrue(Frequency.UNKNOWN.inRange(0));
        assertFalse(Frequency.UNKNOWN.inRange(1));
        assertFalse(Frequency.UNKNOWN.inRange(-1));

        assertTrue(Frequency.TWO_POINT_FOUR.inRange(2412));
        assertTrue(Frequency.TWO_POINT_FOUR.inRange(2472));
        assertFalse(Frequency.TWO_POINT_FOUR.inRange(2411));
        assertFalse(Frequency.TWO_POINT_FOUR.inRange(2473));

        assertTrue(Frequency.TWO_POINT_FOUR_CH_14.inRange(2484));
        assertFalse(Frequency.TWO_POINT_FOUR_CH_14.inRange(2485));
        assertFalse(Frequency.TWO_POINT_FOUR_CH_14.inRange(2483));

        assertTrue(Frequency.FIVE.inRange(5170));
        assertTrue(Frequency.FIVE.inRange(5825));
        assertFalse(Frequency.FIVE.inRange(5169));
        assertFalse(Frequency.FIVE.inRange(5826));
    }

    @Test
    public void testChannel() throws Exception {
        assertEquals(0, Frequency.TWO_POINT_FOUR.channel(1000));

        assertEquals(1, Frequency.TWO_POINT_FOUR.channel(2412));
        assertEquals(13, Frequency.TWO_POINT_FOUR.channel(2472));

        assertEquals(14, Frequency.TWO_POINT_FOUR_CH_14.channel(2484));

        assertEquals(34, Frequency.FIVE.channel(5170));
        assertEquals(165, Frequency.FIVE.channel(5825));
    }

    @Test
    public void testBand() throws Exception {
        assertNull(Frequency.UNKNOWN.wifiBand());
        assertEquals(WiFiBand.TWO_POINT_FOUR, Frequency.TWO_POINT_FOUR.wifiBand());
        assertEquals(WiFiBand.TWO_POINT_FOUR, Frequency.TWO_POINT_FOUR_CH_14.wifiBand());
        assertEquals(WiFiBand.FIVE, Frequency.FIVE.wifiBand());
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(Frequency.UNKNOWN, Frequency.find(1000));

        assertEquals(Frequency.UNKNOWN, Frequency.find(0));
        assertEquals(Frequency.TWO_POINT_FOUR, Frequency.find(2412));
        assertEquals(Frequency.TWO_POINT_FOUR_CH_14, Frequency.find(2484));
        assertEquals(Frequency.FIVE, Frequency.find(5825));
    }

    @Test
    public void testFindChannels() throws Exception {
        assertTrue(Frequency.UNKNOWN.channels().isEmpty());
        assertSetEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}, Frequency.TWO_POINT_FOUR.channels());
        assertSetEquals(new Integer[]{14}, Frequency.TWO_POINT_FOUR_CH_14.channels());
        assertSetEquals(expected5GHZChannels(), Frequency.FIVE.channels());
    }

    @Test
    public void testFindChannel() throws Exception {
        assertEquals(1, Frequency.findChannel(2412));
    }

    @Test
    public void testFind24GHZChannels() throws Exception {
        // setup
        SortedSet<Integer> expected = expected24GHZChannels();
        // execute
        SortedSet<Integer> actual = Frequency.findChannels(WiFiBand.TWO_POINT_FOUR);
        // validate
        assertSetEquals(expected, actual);
    }

    private SortedSet<Integer> expected24GHZChannels() {
        SortedSet<Integer> expected = new TreeSet<>();
        for (int i = 1; i <= 14; i++) {
            expected.add(i);
        }
        return expected;
    }

    @Test
    public void testFind5GHZChannels() throws Exception {
        // setup
        SortedSet<Integer> expected = expected5GHZChannels();
        // execute
        SortedSet<Integer> actual = Frequency.findChannels(WiFiBand.FIVE);
        // validate
        assertSetEquals(expected, actual);
    }

    private SortedSet<Integer> expected5GHZChannels() {
        SortedSet<Integer> expected = new TreeSet<>();
        for (int i = 34; i <= 165; i++) {
            expected.add(i);
        }
        return expected;
    }

    @Test
    public void testFindAllChannels() throws Exception {
        // setup
        SortedSet<Integer> expected = expected24GHZChannels();
        expected.addAll(expected5GHZChannels());
        // execute
        SortedSet<Integer> actual = Frequency.findChannels(WiFiBand.ALL);
        // validate
        assertSetEquals(expected, actual);
    }

    private void assertSetEquals(Integer[] expected, SortedSet<Integer> actual) {
        assertArrayEquals(expected, actual.toArray());
    }

    private void assertSetEquals(SortedSet<Integer> expected, SortedSet<Integer> actual) {
        assertArrayEquals(expected.toArray(), actual.toArray());
    }
}