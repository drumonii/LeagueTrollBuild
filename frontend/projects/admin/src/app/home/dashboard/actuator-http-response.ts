export interface ActuatorHttpResponse {
  measurements: HttpMeasurement[];
  availableTags: HttpAvailableTag[];
}

interface HttpMeasurement {
  statistic: string;
  value: number;
}

interface HttpAvailableTag {
  tag: string;
  values: string[];
}
