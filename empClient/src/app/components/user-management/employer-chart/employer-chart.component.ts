import { Component, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { JobRequestService } from 'src/app/service/job-request.service';

@Component({
  selector: 'app-employer-chart',
  templateUrl: './employer-chart.component.html',
  styleUrls: ['./employer-chart.component.css']
})
export class EmployerChartComponent implements OnInit {

  constructor(private requestService:JobRequestService) { }

  myChart: any;

  labels: Array<string> = new Array<string>();
  count: Array<number> = new Array<number>();

  ngOnInit(): void {
    this.userCounts()
  }


  userCounts() {
    while (this.labels.length) {
      this.labels.splice(0, 1);
      this.count.splice(0, 1);
    }
    this.requestService.getRequestStatus().subscribe({
      next: (res: any) => {
        console.log(res);
        
        const map1 = new Map(Object.entries(res));

        for (let key of map1.keys()) {
          this.labels.push(key);
        }

        for (let key of map1.values()) {
          this.count.push(parseInt(key as string));
        }
        this.getChart()
      },
      error: (err: any) => {
        console.log(err);
      },
    });
  }



  getChart() {
    Chart.register(...registerables);
    const myChart = new Chart('myRequestChart', {
      type: 'line',
      data: {
        labels: this.labels,
        datasets: [
          {
            label: 'No of Requests',
            data: this.count,
            fill: true,
            // borderColor: '#c9ac7d',
            tension: 0.3,
            backgroundColor: [
              'rgba(255, 99, 132, 0.2)',
              'rgba(54, 162, 235, 0.2)',
              'rgba(255, 206, 86, 0.2)',
              'rgba(75, 192, 192, 0.2)',
              'rgba(153, 102, 255, 0.2)',
              'rgba(255, 159, 64, 0.2)',
            ],
            borderColor: [
              'rgba(255, 99, 132, 1)',
              'rgba(54, 162, 235, 1)',
              'rgba(255, 206, 86, 1)',
              'rgba(75, 192, 192, 1)',
              'rgba(153, 102, 255, 1)',
              'rgba(255, 159, 64, 1)',
            ],
            borderWidth: 3,
          },
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    });
  }
}
