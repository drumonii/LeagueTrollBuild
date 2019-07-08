export interface ActuatorResponse {
  name: string;
  measurements: ActuatorMeasurement[];
}

interface ActuatorMeasurement {
  statistic: string;
  value: number;
}
