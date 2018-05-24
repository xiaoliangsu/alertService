package model;

public class ALertInfoBean {

        private String id;
        private String eventType;
        private String siteToken;
        private String deviceAssignmentToken;
        private String assignmentType;
        private String assetModuleId;
        private String assetId;
        private String eventDate;
        private String receivedDate;
        private String source;
        private String level;
        private String type;
        private String message;

    public String getSitewhereToken() {
        return sitewhereToken;
    }

    public void setSitewhereToken(String sitewhereToken) {
        this.sitewhereToken = sitewhereToken;
    }

    public String sitewhereToken;
        public ALertInfoBean(){}
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }
        public String getEventType() {
            return eventType;
        }

        public void setSiteToken(String siteToken) {
            this.siteToken = siteToken;
        }
        public String getSiteToken() {
            return siteToken;
        }

        public void setDeviceAssignmentToken(String deviceAssignmentToken) {
            this.deviceAssignmentToken = deviceAssignmentToken;
        }
        public String getDeviceAssignmentToken() {
            return deviceAssignmentToken;
        }

        public void setAssignmentType(String assignmentType) {
            this.assignmentType = assignmentType;
        }
        public String getAssignmentType() {
            return assignmentType;
        }

        public void setAssetModuleId(String assetModuleId) {
            this.assetModuleId = assetModuleId;
        }
        public String getAssetModuleId() {
            return assetModuleId;
        }

        public void setAssetId(String assetId) {
            this.assetId = assetId;
        }
        public String getAssetId() {
            return assetId;
        }

        public void setEventDate(String eventDate) {
            this.eventDate = eventDate;
        }
        public String getEventDate() {
            return eventDate;
        }

        public void setReceivedDate(String receivedDate) {
            this.receivedDate = receivedDate;
        }
        public String getReceivedDate() {
            return receivedDate;
        }

        public void setSource(String source) {
            this.source = source;
        }
        public String getSource() {
            return source;
        }

        public void setLevel(String level) {
            this.level = level;
        }
        public String getLevel() {
            return level;
        }

        public void setType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }

        public void setMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
}
