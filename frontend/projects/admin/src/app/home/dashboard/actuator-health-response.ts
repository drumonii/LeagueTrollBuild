export interface HealthResponse {
  components: HealthComponents;
}

interface HealthComponents {
  diskSpace: DiskSpace;
}

interface DiskSpace {
  details: DiskSpaceDetails;
}

interface DiskSpaceDetails {
  total: number;
  free: number;
}
