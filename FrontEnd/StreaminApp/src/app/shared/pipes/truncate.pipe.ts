import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  standalone: true,
  name: 'truncate'
})
export class TruncatePipe implements PipeTransform {
  public transform(value: string | undefined, limit: number = 100): string {
    if (!value) return '';
    return value.length > limit ? `${value.slice(0, limit)}...` : value;
  }
}
