package org.testcontainers.junit.jupiter;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class TestcontainersNestedRestartedContainerTests {

    @Container
    private final GenericContainer topLevelContainer = new GenericContainer("httpd:2.4-alpine")
        .withExposedPorts(80);

    private static String topLevelContainerId;

    private static String nestedContainerId;

    @Test
    void top_level_container_should_be_running() {
        assertTrue(topLevelContainer.isRunning());
        topLevelContainerId = topLevelContainer.getContainerId();
    }

    @Nested
    class NestedTestCase {

        @Container
        private final GenericContainer nestedContainer = new GenericContainer("httpd:2.4-alpine")
            .withExposedPorts(80);

        @Test
        void both_containers_should_be_running() {
            assertTrue(topLevelContainer.isRunning());
            assertTrue(nestedContainer.isRunning());

            if (nestedContainerId == null) {
                nestedContainerId = nestedContainer.getContainerId();
            } else {
                assertNotEquals(nestedContainerId, nestedContainer.getContainerId());
            }
        }

        @Test
        void containers_should_not_be_the_same() {
            assertNotEquals(topLevelContainer.getContainerId(), nestedContainer.getContainerId());

            if (nestedContainerId == null) {
                nestedContainerId = nestedContainer.getContainerId();
            } else {
                assertNotEquals(nestedContainerId, nestedContainer.getContainerId());
            }
        }

        @Test
        void ids_should_not_change() {
            assertNotEquals(topLevelContainerId, topLevelContainer.getContainerId());

            if (nestedContainerId == null) {
                nestedContainerId = nestedContainer.getContainerId();
            } else {
                assertNotEquals(nestedContainerId, nestedContainer.getContainerId());
            }
        }
    }
}
