package com.example.myproject.repository;

import com.example.myproject.domain.FileList;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;

import static com.example.myproject.domain.QFileList.fileList;
import static com.example.myproject.domain.QMusicList.musicList;

@RequiredArgsConstructor
public class FileListRepositoryImpl implements FileListRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FileList> findByMusicListIdQueryDsl(Long musicListId) {
        return queryFactory
                .selectFrom(fileList)
                .innerJoin(fileList.musicList, musicList)
                .fetchJoin()
                .where(musicList.id.eq(musicListId))
                .fetch();
    }

    @Override
    public FileList findByStoredFilenameQueryDsl(String storedFilename) {
        return queryFactory
                .selectFrom(fileList)
                .where(fileList.storedFilename.eq(storedFilename))
                .fetchOne();
    }
}
