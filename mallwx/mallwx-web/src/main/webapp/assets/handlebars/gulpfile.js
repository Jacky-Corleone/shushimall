var gulp = require('gulp');
var handlebars = require('gulp-handlebars');
var wrap = require('gulp-wrap');
var declare = require('gulp-declare');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
// var sourcemaps = require('gulp-sourcemaps');

// 合并 hbs 模版
gulp.task('odc-templates', function(){
  console.log("concat templates");
  gulp.src('js/odc/templates/*.hbs')
    .pipe(handlebars())
    .pipe(wrap('(function() { var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {}; templates[\'<%= getName(file.relative) %>\'] = template(<%= contents %>);})();', {}, {
            imports: {
                getName: function(filename) {
                    var path = require('path');
                    return path.basename(filename, path.extname(filename));
                }
            }
        }))
    .pipe(concat('templates.js'))
    .pipe(gulp.dest('js/odc'));
});
gulp.task('sc-templates', function(){
	  console.log("concat templates");
    gulp.src('template/productdetail/*.hbs')
	    .pipe(handlebars())
	    .pipe(wrap('(function() { var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {}; templates[\'<%= getName(file.relative) %>\'] = template(<%= contents %>);})();', {}, {
	            imports: {
	                getName: function(filename) {
	                    var path = require('path');
	                    return path.basename(filename, path.extname(filename));
	                }
	            }
	        }))
	    .pipe(concat('templates.js'))
	    .pipe(gulp.dest('../handlebars'));
	});
// 合并 js 模块
gulp.task('odc-main', function(){
      console.log("concat module");

	  gulp.src('js/odc/module/*.js')
        // .pipe(sourcemaps.init())
	    .pipe(concat('main.js'))
        // .pipe(sourcemaps.write())
	    .pipe(gulp.dest('js/odc'));
	});

// 压缩
gulp.task('odc-build',function(){
    gulp.src('js/odc/main.js')
    .pipe(uglify('main.min.js'))
    .pipe(gulp.dest('js/odc'));

    gulp.src('js/odc/templates.js')
    .pipe(uglify('templates.min.js'))
    .pipe(gulp.dest('js/odc'));
});

gulp.task('odc-vendor',function(){
    gulp.src('js/vendor/*.js')
        .pipe(concat('vendor.js'))
        .pipe(uglify())
        .pipe(gulp.dest('js/odc'));
});

gulp.task('watch-product',function() {
    gulp.watch('template/productdetail/*.hbs', ['sc-templates']);
	// gulp.watch('js/odc/module/*.js',['odc-main']);
    // gulp.watch('js/odc/main.js',['odc-compress']);
    // gulp.watch('js/odc/templates.js',['odc-compress']);

});
