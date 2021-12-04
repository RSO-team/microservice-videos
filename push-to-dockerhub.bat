docker build -t lgaljo/rt_basketball_videos -f Dockerfile_with_maven_build .
docker tag lgaljo/rt_basketball_videos lgaljo/rt_basketball_videos:latest
docker push -a lgaljo/rt_basketball_videos