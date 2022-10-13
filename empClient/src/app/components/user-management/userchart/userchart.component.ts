import { Component, OnInit } from '@angular/core';
import { JobService } from 'src/app/service/job.service';
import { Chart, registerables } from 'chart.js';
@Component({
  selector: 'app-userchart',
  templateUrl: './userchart.component.html',
  styleUrls: ['./userchart.component.css'],
})
export class UserchartComponent implements OnInit {
  constructor(private service: JobService) {}
  myChart: any;

  ngOnInit(): void {
    this.getChart();
  }

  getChart() {
    Chart.register(...registerables);
    const myChart = new Chart('myChart', {
      type: 'line',
      data: {
        labels: [
          'User 1',
          'User 2',
          'User 3',
          'User 4',
          'User 5',
          'User 6',
          'User 7',
          'User 7',
          'User 7',
        ],
        datasets: [
          {
            label: 'My First Dataset',
            data: [65, 59,100, 80, 81, 56, 55, 40,100],
            fill: false,
            borderColor: '#c9ac7d',
            tension: 0.1,
          },
        ],
      },
    });
  }
}
