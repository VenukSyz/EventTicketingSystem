/**
 * Contains all the API endpoints and WebSocket paths used in the application.
 * Organized into different sections based on functionality, such as configuration,
 * control panel, event management, and WebSocket communication.
 */
export const Constant = {
    /**
     * API methods related to configuration management.
     */
    CONFIG_API_METHODS: {
        /** Endpoint to fetch all configurations. */
        GET_ALL_CONFIGS: "/configuration/getAllConfigurations",
        /** Endpoint to save or update a configuration. */
        SAVE_UPDATE_CONFIG: "/configuration/saveUpdateConfiguration",
        /** Endpoint to delete a specific configuration. */
        DELETE_CONFIG: "/configuration/deleteConfiguration/"
    },
    /**
     * API methods related to control panel operations.
     */
    CONTROL_PANEL_API_METHODS: {
        /** Endpoint to start the control panel. */
        START: "/controlPanel/start",
        /** Endpoint to resume the control panel. */
        RESUME: "/controlPanel/resume",
        /** Endpoint to stop the control panel. */
        STOP: "/controlPanel/stop"
    },
    /**
     * API methods related to event management.
     */
    EVENT_API_METHODS: {
        /** Endpoint to fetch all events. */
        GET_ALL_EVENTS: "/event/getAllEvents",
        /** Endpoint to delete a specific event. */
        DELETE_EVENT: "/event/deleteEvent/"
    },
    /**
     * WebSocket paths for communication.
     */
    WEBSOCKET_PATHS: {
        /** WebSocket path for logging messages. */
        LOG_PATH: "/topic/logs",
        /** WebSocket path for status updates. */
        STATUS_PATH: "/topic/status"
    }
}