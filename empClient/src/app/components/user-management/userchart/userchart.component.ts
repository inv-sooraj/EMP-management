import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { JobService } from 'src/app/service/job.service';
import { UserService } from 'src/app/service/user.service';
@Component({
  selector: 'app-userchart',
  templateUrl: './userchart.component.html',
  styleUrls: ['./userchart.component.css'],
})
export class UserchartComponent implements OnInit {
  constructor(private userService: UserService, private jobService: JobService) { }
  myChart: any;
  userDay: Array<string> = new Array<string>();
  userCount: Array<number> = new Array<number>();
  jobDay: Array<string> = new Array<string>();
  jobCount: Array<number> = new Array<number>();

  days: number = 7;

  ngOnInit(): void {
    this.userCounts();
    this.getPieChart()
  }

  changeDays() {
    console.log(this.days);
    let chartStatus = Chart.getChart("myChart"); // <canvas> id
    if (chartStatus != undefined) {
      chartStatus.destroy();
    }
    this.userCounts();
  }

  userCounts() {

    while (this.userDay.length) {
      this.userDay.splice(0, 1);
      this.jobDay.splice(0, 1);
      this.jobCount.splice(0, 1);
      this.userCount.splice(0, 1);
    }


    let queryParams = new HttpParams()
      .append('days', this.days);
    this.userService.getUserCount(queryParams).subscribe({
      next: (res: any) => {
        const map1 = new Map(Object.entries(res));

        for (let key of map1.keys()) {
          this.userDay.push(key)
        }

        for (let key of map1.values()) {
          this.userCount.push(parseInt(key as string))
        }
        this.jobCounts();
      },
      error: (err: any) => {
        console.log(err);
      }
    })
  }

  jobCounts() {
    let queryParams = new HttpParams()
      .append('days', this.days)
    this.jobService.getJobCount(queryParams).subscribe({
      next: (res: any) => {

        const map2 = new Map(Object.entries(res));

        for (let key of map2.keys()) {
          this.jobDay.push(key)
        }

        for (let key of map2.values()) {
          this.jobCount.push(parseInt(key as string))
        }
        this.getChart();
      },
      error: (err: any) => {
        console.log(err);
      }
    })
  }


  getChart() {
    Chart.register(...registerables);
    const myChart = new Chart('myChart', {
      type: 'line',
      data: {

        labels: this.getUserDates,
        datasets: [
          {
            label: 'Users Registered',
            data: this.getUserCounts,
            fill: true,
            // borderColor: '#c9ac7d',
            tension: 0.3,
            backgroundColor: [
              'rgba(255, 99, 132, 0.2)',
              'rgba(54, 162, 235, 0.2)',
              'rgba(255, 206, 86, 0.2)',
              'rgba(75, 192, 192, 0.2)',
              'rgba(153, 102, 255, 0.2)',
              'rgba(255, 159, 64, 0.2)'
            ],
            borderColor: [
              'rgba(255, 99, 132, 1)',
              'rgba(54, 162, 235, 1)',
              'rgba(255, 206, 86, 1)',
              'rgba(75, 192, 192, 1)',
              'rgba(153, 102, 255, 1)',
              'rgba(255, 159, 64, 1)'
            ],
            borderWidth: 3
          },

          {
            label: 'Job Posted',
            data: this.getJobCounts,
            fill: true,
            // borderColor: '#c9ac7d',
            tension: 0.1,
            backgroundColor: [
              'rgba(255, 99, 132, 0.2)',
              'rgba(54, 162, 235, 0.2)',
              'rgba(255, 206, 86, 0.2)',
              'rgba(75, 192, 192, 0.2)',
              'rgba(153, 102, 255, 0.2)',
              'rgba(255, 159, 64, 0.2)'
            ],
            borderColor: [
              'rgba(255, 99, 132, 1)',
              'rgba(54, 162, 235, 1)',
              'rgba(255, 206, 86, 1)',
              'rgba(75, 192, 192, 1)',
              'rgba(153, 102, 255, 1)',
              'rgba(255, 159, 64, 1)'
            ],
            borderWidth: 3
          },
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });

  }






  get getUserCounts() {
    console.log("list", this.userCount);

    return this.userCount
  }


  get getUserDates() {
    console.log("list", this.userDay);

    return this.userDay
  }


  get getJobCounts() {
    console.log("list", this.userCount);

    return this.jobCount
  }


  get getJobDates() {
    console.log("list", this.jobDay);

    return this.userDay
  }


  getPieChart() {
    Chart.register(...registerables);
    const myPieChart = new Chart('myPieChart', {
      type: 'pie',
      data: {
        labels: [
          'Users',
          'Jobs',
          'Requests'
        ],
        datasets: [{
          label: 'My First Dataset',
          data: [5, 20, 10],
          backgroundColor: [
            'rgb(255, 99, 132)',
            'rgb(54, 162, 235)',
            'rgb(255, 205, 86)'
          ],
          hoverOffset: 4
        }]
      }
    });
  }
}
