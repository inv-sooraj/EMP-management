import { Component, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { UserService } from 'src/app/service/user.service';
@Component({
  selector: 'app-userchart',
  templateUrl: './userchart.component.html',
  styleUrls: ['./userchart.component.css'],
})
export class UserchartComponent implements OnInit {
  constructor(private service: UserService) { }
  myChart: any;
  day: Array<string> = new Array<string>();
  count: Array<number> = new Array<number>();

  ngOnInit(): void {
    this.userCounts();
  }

  userCounts() {
    this.service.getUserCount().subscribe({
      next: (res: any) => {
        console.log(res);

        const map1 = new Map(Object.entries(res));

        console.log(map1.values());
        console.log(map1.keys());

        for(let key of map1.keys()) {
          console.log(key);
          this.day.push(key)
       }

       for(let key of map1.values()) {
        // console.log(key);
        this.count.push(parseInt(key as string))
     }


        // this.setCounts(map1.values());
        // this.setDates(map1.keys());
        this.getChart();
      },
      error: (err: any) => {
        console.log(err);
      }
    })
  }

  // setData() {

  //   let ff = new Date()
  //   console.log(ff.getDate() - 1 + "-" + ff.getMonth() + "-" + ff.getFullYear());

  //   for (let i = 0; i <= 6; i++) {
  //     this.dates[i] = ff.getDate() - 1 + "-" + ff.getMonth() + "-" + ff.getFullYear()
  //     console.log(this.dates[i]);

  //   }
  //   this.myChart.data.datasets = this.dates
  //   this.myChart.update()

  // }



  getChart() {
    Chart.register(...registerables);
    const myChart = new Chart('myChart', {
      type: 'line',
      data: {

        labels: this.getDates,
        datasets: [
          {
            label: 'No of users registered',
            data: this.getCounts,
            // fill: false,
            borderColor: '#c9ac7d',
            // tension: 0.1,
          },
        ],
      },
    });
  }



  setCounts(count: any) {



    this.count = count;
    console.log("list **", this.count);

  }

  get getCounts() {
    console.log("list", this.count);

    return this.count
  }

  setDates(dates: any) {
    this.day = dates;
    console.log("list **", this.day);

  }

  get getDates() {
    console.log("list", this.day);

    return this.day
  }
}
