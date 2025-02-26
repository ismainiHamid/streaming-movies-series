import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import Plyr from 'plyr';
import {MediaService} from '../../../services/media.service';
import {ActivatedRoute, Params} from '@angular/router';
import Hls from 'hls.js';

@Component({
  selector: 'app-watch-media',
  standalone: false,

  templateUrl: './watch-media.component.html',
  styleUrl: './watch-media.component.css'
})
export class WatchMediaComponent implements OnInit{
  @ViewChild('videoPlayer', {static: true}) public videoElement!: ElementRef<HTMLVideoElement>;
  protected player!: Plyr;

  constructor(private mediaService: MediaService, private activatedRoute: ActivatedRoute) {
  }

  public ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params: Params): void => {
      const mediaId: string | null = params['q'] || null;
      if (mediaId != null)
        this.mediaService.getMasterFileByMediaId(mediaId).subscribe({
          next: (data: { segment: string }): void => this.initializePlayer(data.segment),
          error: (err: any): void => console.error(err),
        });
    });
  }

  public initializePlayer(segment: string): void {
    if (Hls.isSupported()) {
      const hls = new Hls();
      hls.loadSource('/cinewave/api/v1/medias/watch/' + segment);
      hls.attachMedia(this.videoElement.nativeElement);
      hls.on(Hls.Events.MANIFEST_PARSED, (): void => {
        const availableQualities = hls.levels.map(level => level.height);
        availableQualities.unshift(0);
        if (this.videoElement && !this.player) {
          this.player = new Plyr(this.videoElement.nativeElement, {
            controls: ['play', 'airplay', 'progress', 'current-time', 'duration', 'mute', 'volume', 'caption', 'pip', 'fullscreen', 'settings'],
            settings: ['quality', 'speed'],
            speed: {
              selected: 1,
              options: [0.5, 0.75, 1, 1.25, 1.5, 2],
            },
            quality: {
              default: 0,
              options: availableQualities,
              forced: true,
              onChange: (quality: number): void => {
                if (quality === 0)
                  hls.currentLevel = -1;
                else
                  hls.currentLevel = hls.levels.findIndex(level => level.height === quality);
              }
            },
            i18n: {
              quality: 'Quality',
              speed: 'Speed',
              qualityLabel: {0: 'Auto',}
            },
            captions: {active: true, language: 'en'},
          });
        }
      });

      hls.on(Hls.Events.ERROR, (event: any, data: any): void => {
        if (data.fatal) console.error('HLS.js Error:', data, event);
      });
    } else if (this.videoElement.nativeElement.canPlayType('application/vnd.apple.mpegurl'))
      this.videoElement.nativeElement.src = '/cinewave/api/v1/medias/watch/' + segment;
  }
}
