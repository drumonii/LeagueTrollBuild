import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { FooterComponent } from './footer.component';
import { Version } from '@model/version';
import { VersionsService } from '@service/versions.service';

describe('FooterComponent', () => {
  let component: FooterComponent;
  let fixture: ComponentFixture<FooterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FooterComponent],
      providers: [VersionsService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FooterComponent);
    component = fixture.componentInstance;
  });

  it('should show latest saved Version in footer', inject([VersionsService], (versionsService: VersionsService) => {
    const latestVersion = new Version('8.2.1');

    spyOn(versionsService, 'getLatestVersion').and.returnValue(of(latestVersion));

    fixture.detectChanges();

    const latestVersionDe = fixture.debugElement.query(By.css('#latest-saved-version'));
    expect(latestVersionDe.nativeElement.textContent.trim()).toBe(`Patch ${latestVersion.patch}`);

    const aboutDe = fixture.debugElement.query(By.css('#about'));
    expect(aboutDe).toBeTruthy();

    const githubLinkDe = fixture.debugElement.query(By.css('#github-link'));
    expect(githubLinkDe.nativeElement.textContent.trim()).toBe('GitHub');
    expect(githubLinkDe.nativeElement.href).toBe('https://github.com/drumonii/LeagueTrollBuild');

    const disclaimerDe = fixture.debugElement.query(By.css('#disclaimer'));
    expect(disclaimerDe).toBeTruthy();
  }));
});
