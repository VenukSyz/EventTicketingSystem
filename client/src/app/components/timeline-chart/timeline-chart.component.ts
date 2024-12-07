import { Component, OnInit, ViewChild } from '@angular/core';
import { ChartDataset, ChartOptions, ChartType, Chart, registerables } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import * as dayjs from 'dayjs';
import 'chartjs-adapter-moment'


Chart.register(...registerables)

@Component({
  selector: 'app-timeline-chart',
  standalone: true,
  imports: [BaseChartDirective],
  templateUrl: './timeline-chart.component.html',
  styleUrl: './timeline-chart.component.css'
})
export class TimelineChartComponent implements OnInit{
  @ViewChild(BaseChartDirective) chart?: BaseChartDirective;

  soldTickets: number = 0;
  
  ngOnInit(): void {
    debugger;
    this.loadChartDataFromSession();
  }

  public lineChartData: ChartDataset[] = [
    {
      data: [],
      label: 'Tickets Sold',
      fill: true,
      borderColor: 'blue',
      tension: 0.1,
      pointBackgroundColor: 'rgba(0, 0, 0, 0.47)', // Fill color of data points
      pointBorderColor: 'black', // Border color of data points
      pointRadius: 4, // Radius of data points
      pointHoverBackgroundColor: 'black', // Background color when hovered
      pointHoverBorderColor: 'white', // Border color when hovered
    }
  ];

  public lineChartLabels: string[] = [];

  public lineChartOptions: ChartOptions = {
    responsive: true,
    plugins: {
      legend: {
        labels: {
          color: 'black'
        }
      },
      tooltip: {
        backgroundColor: 'rgb(255, 255, 255)', // Tooltip background color
        titleColor: 'black', // Tooltip title text color
        bodyColor: 'black' // Tooltip body text color
      }
    },
    scales: {
      x: {
        type: 'time',  // Time-based X axis
        time: {
          unit: 'second',  // Use seconds as the unit
          tooltipFormat: 'll HH:mm:ss',  // Format for tooltips
          displayFormats: {
            second: 'MM-DD', // Display format for seconds
          }
        },
        grid: {
          color: 'black'
        },
        ticks: {
          color: 'black'
        },
        title: {
          display: true,
          text: 'Time of Sale',
          color: 'black'
        }
      },
      y: {
        title: {
          display: true,
          text: 'Tickets Sold',
          color: 'black'
        },
        grid: {
          color: 'black'
        },
        ticks: {
          color: 'black'
        },
      }
    },
  };

  public lineChartLegend = true;
  public lineChartType: ChartType = 'line';

  public updateChart(newTicketsSold: number): void {
    // Create the timestamp (current time) for each new data point
    const currentTimestamp = new Date().toISOString(); // Full timestamp including seconds
    // Add the new timestamp and ticketsSold data to the chart
    this.lineChartLabels.push(currentTimestamp);  // Add the timestamp to labels
    this.lineChartData[0].data.push(newTicketsSold - this.soldTickets);  // Add the tickets sold to data

    this.soldTickets = newTicketsSold;

    // Save updated data to sessionStorage
    this.saveChartDataToSession();

    if (this.chart && this.chart.chart) {
      this.chart.chart.update(); // This forces Chart.js to re-render the chart
    }
  }

  public resetChart(): void {
    // Clear chart data and labels
    this.lineChartLabels = [];
    this.lineChartData[0].data = [];

    // Reset the soldTickets counter
    this.soldTickets = 0;

    sessionStorage.removeItem('chartData');

    // Update the chart if it exists
    if (this.chart && this.chart.chart) {
      this.chart.chart.update();
    }
  }

  private saveChartDataToSession(): void {
    const chartData = {
      labels: this.lineChartLabels,
      data: this.lineChartData[0].data,
      soldTickets: this.soldTickets
    };
    sessionStorage.setItem('chartData', JSON.stringify(chartData));
  }

  private loadChartDataFromSession(): void {
    const storedData = sessionStorage.getItem('chartData');
    if (storedData) {
      const { labels, data, soldTickets } = JSON.parse(storedData);
      this.lineChartLabels = labels || [];
      this.lineChartData[0].data = data || [];
      this.soldTickets = soldTickets || 0;
    }
  }
}
