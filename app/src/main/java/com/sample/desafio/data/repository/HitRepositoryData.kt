package com.sample.desafio.data.repository

import com.sample.desafio.data.datasource.HitRemoteDataSource
import com.sample.desafio.data.datasource.local.db.HitsDao
import com.sample.desafio.data.datasource.local.db.toListDomain
import com.sample.desafio.data.datasource.local.db.toListEntity
import com.sample.desafio.domain.HitDomain
import com.sample.desafio.domain.commons.functional.Either
import com.sample.desafio.domain.commons.functional.Failure
import com.sample.desafio.domain.commons.functional.onSuccess
import com.sample.desafio.domain.repository.HitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class HitRepositoryData @Inject constructor(
    private val remoteDataSource: HitRemoteDataSource,
    private val hitsDao: HitsDao
) : HitRepository {

    override suspend fun getHits(): Either<Failure, List<HitDomain>> {
        return remoteDataSource.getHits().onSuccess { hitsDomain ->
            runBlocking { saveHits(hitsDomain) }
        }
    }

    private suspend fun saveHits(hitsDomain: List<HitDomain>) {
        val hitsUpdate = hitsDao.getAllIdHitsDeleted()
        val insertHits = hitsDomain.filterNot { item -> hitsUpdate.contains(item.id) }
        hitsDao.insert(insertHits.toListEntity())
    }

    override fun getHitsStream(): Flow<List<HitDomain>> {
        return hitsDao.getHitsStream().map { entities ->
            return@map entities.toListDomain()
        }
    }

    override suspend fun deleteHit(id: String): Either<Failure, Unit> {
        hitsDao.updateStatus(id)
        return Either.Right(Unit)
    }
}