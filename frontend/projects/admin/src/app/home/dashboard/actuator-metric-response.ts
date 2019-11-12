export interface MetricsResponse {
  name: string;
  measurements: MetricMeasurement[];
}

interface MetricMeasurement {
  statistic: string;
  value: number;
}
