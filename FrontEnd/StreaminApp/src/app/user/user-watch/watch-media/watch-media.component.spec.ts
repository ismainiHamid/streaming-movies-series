import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WatchMediaComponent } from './watch-media.component';

describe('WatchMediaComponent', () => {
  let component: WatchMediaComponent;
  let fixture: ComponentFixture<WatchMediaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WatchMediaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WatchMediaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
