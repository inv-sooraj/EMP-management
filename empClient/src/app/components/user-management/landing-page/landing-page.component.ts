import { ViewportScroller } from '@angular/common';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css'],
})
export class LandingPageComponent implements OnInit {
  // flag: boolean = false;

  constructor(private viewportScroller: ViewportScroller) {}

  ngOnInit(): void {}

  onClickScroll(elementId: string): void {
    // this.flag = true;
    this.viewportScroller.scrollToAnchor(elementId);
  }
  onClickScrollAbout(elementId: string): void {
    // this.flag = true;
    this.viewportScroller.scrollToAnchor(elementId);
  }
  onClickScrollContact(elementId: string): void {
    this.viewportScroller.scrollToAnchor(elementId);
  }
}
